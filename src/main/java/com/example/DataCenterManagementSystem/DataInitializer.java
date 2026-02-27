package com.example.DataCenterManagementSystem;

import com.example.DataCenterManagementSystem.config.security.CustomUserDetailsService;
import com.example.DataCenterManagementSystem.dto.dcim.*;
import com.example.DataCenterManagementSystem.entity.dcim.EquipmentType;
import com.example.DataCenterManagementSystem.entity.security.User;
import com.example.DataCenterManagementSystem.entity.security.UserRole;
import com.example.DataCenterManagementSystem.repository.UserRepository;
import com.example.DataCenterManagementSystem.service.*;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import java.util.ArrayList;
import java.util.List;

@Component
@RequiredArgsConstructor
public class DataInitializer {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    private final LocationService locationService;
    private final DataCenterService dataCenterService;
    private final RackRowService rackRowService;
    private final RackService rackService;
    private final EquipmentService equipmentService;
    private final PortConnectionService portConnectionService;

    private final CustomUserDetailsService userDetailsService;

    @Bean
    CommandLineRunner initData() {
        return args -> {
            User admin = createAdminIfNotExists();

            UserDetails userDetails = userDetailsService.loadUserByUsername(admin.getUsername());

            SecurityContextHolder.getContext().setAuthentication(
                    new UsernamePasswordAuthenticationToken(admin, null,userDetails.getAuthorities() )
            );

            System.out.println("Start Seeding");

            // ۳. ایجاد درختواره مکان (ایران + ۵ استان)
            Long iranId = createLocation("ایران", 1, null);
            Long tehranId = createLocation("تهران", 2, iranId);
            Long isfahanId = createLocation("اصفهان", 2, iranId);
            Long farsId = createLocation("فارس", 2, iranId);
            Long khorasanId = createLocation("خراسان رضوی", 2, iranId);
            Long azerbaijanId = createLocation("آذربایجان شرقی", 2, iranId);

            List<Long> provinceIds = List.of(tehranId, isfahanId, farsId, khorasanId, azerbaijanId);
            String[] provinceNames = {"تهران", "اصفهان", "فارس", "خراسان رضوی", "آذربایجان شرقی"};

            // ۴. ایجاد ۵ مرکز داده + ساختار کامل هر رک
            for (int i = 0; i < 5; i++) {
//                System.out.println("📍 ایجاد مرکز داده " + (i + 1) + " در " + provinceNames[i]);

                CreateDataCenterRequest dcDto = new CreateDataCenterRequest();
                dcDto.setName("DC-" + provinceNames[i]);
                dcDto.setLocationId(provinceIds.get(i));

                DataCenterResponse savedDc = dataCenterService.createDataCenter(dcDto);

                for (int rowNum = 1; rowNum <= 5; rowNum++) {
                    CreateRackRowRequest rowDto = new CreateRackRowRequest();
                    rowDto.setRowName("Row-" + rowNum);
                    rowDto.setDataCenterId(savedDc.getId());

                    RackRowResponse savedRow = rackRowService.createRackRow(rowDto);

                    for (int rackNum = 1; rackNum <= 10; rackNum++) {
                        CreateRackRequest rackDto = new CreateRackRequest();
                        rackDto.setRackNumber(rackNum);
                        rackDto.setRackRowId( savedRow.getId());

                        RackResponse savedRack = rackService.createRack(rackDto);
                        Long rackId = savedRack.getId();

                        // سوئیچ (یونیت ۴۲ - ۲۴ پورت)
                        Long switchId = createEquipment(rackId, "Switch-24P", EquipmentType.SWITCH,
                                1, 42, 24);

                        // پچ‌پنل (یونیت ۴۱ - ۲۴ پورت)
                        Long patchId = createEquipment(rackId, "PatchPanel-24P",
                                EquipmentType.PATCH_PANEL, 1, 41, 24);

                        // ۳. سرورها با محاسبه خودکار startUnit
                        int currentUnit = 1;   // از پایین شروع می‌کنیم

                        // ۵ سرور ۱U
                        for (int j = 1; j <= 5; j++) {
                            currentUnit = placeServer(rackId, "Server-1U-" + j, 1, currentUnit);
                        }
                        // ۵ سرور ۲U
                        for (int k = 1; k <= 5; k++) {
                            currentUnit = placeServer(rackId, "Server-2U-" + k, 2, currentUnit);
                        }
                        // ۲ سرور ۴U
                        for (int c = 1; c <= 2; c++) {
                            currentUnit = placeServer(rackId, "Server-4U-" + c, 4, currentUnit);
                        }

                        // ۴. اتصال پورت اول همه سرورها از طریق پچ‌پنل به سوئیچ
                        connectAllServersToSwitch(rackId, patchId, switchId);
                    }
                }
            }

            System.out.println("Seed is done!");
            System.out.println("5 data centers + 250 racks + complete equipment + correct connections were created.");
        };
    }

    private int placeServer(Long rackId, String name, int unitSize, int startFrom) {
        CreateEquipmentRequest req = new CreateEquipmentRequest(
                name, EquipmentType.SERVER, unitSize, rackId, startFrom, 4
        );
        EquipmentResponse equipment = equipmentService.createEquipment(req);
        Long serverId = equipment.id();
        return startFrom + unitSize;   // برای سرور بعدی
    }

    private void connectAllServersToSwitch(Long rackId, Long patchId, Long switchId) {
        List<EquipmentResponse> servers = equipmentService.getEquipmentsByRackId(rackId)
                .stream()
                .filter(e -> e.type() == EquipmentType.SERVER)
                .toList();

        int patchPortIndex = 1;   // از پورت ۱ پچ شروع می‌کنیم
        int switchPortIndex = 1;

        for (EquipmentResponse server : servers) {
            Long serverPort1 = server.portIds().get(0);           // پورت اول سرور

            // ── اتصال ۱: سرور → پورت پچ (ورودی)
            Long patchPortForServer = getPortId(patchId, patchPortIndex);
            portConnectionService.connectPorts(serverPort1, patchPortForServer);

            // ── اتصال ۲: پورت بعدی پچ → سوئیچ (خروجی)
            patchPortIndex++;
            Long patchPortForSwitch = getPortId(patchId, patchPortIndex);
            Long switchPortId = getPortId(switchId, switchPortIndex);

            portConnectionService.connectPorts(patchPortForSwitch, switchPortId);

            // آماده‌سازی برای سرور بعدی
            patchPortIndex++;
            switchPortIndex++;

            // چرخش پورت‌ها (۲۴ پورت داریم)
            if (patchPortIndex > 24) patchPortIndex = 1;
            if (switchPortIndex > 24) switchPortIndex = 1;
        }
    }
    private User createAdminIfNotExists() {
        return userRepository.findByUsername("admin")
                .orElseGet(() -> {
                    User admin = User.builder()
                            .username("admin")
                            .password(passwordEncoder.encode("12345678"))
                            .role(UserRole.ADMIN)
                            .build();
                    return userRepository.save(admin);
                });
    }

    private Long createLocation(String name, int level, Long parentId) {
        CreateLocationRequest dto = new CreateLocationRequest(name, level, parentId);
        LocationResponse location = locationService.createLocation(dto);
        return location.id();
    }

    private Long createEquipment(Long rackId, String name, EquipmentType type, int unitSize, int startUnit, int portCount) {
        CreateEquipmentRequest req = new CreateEquipmentRequest(
                name, type, unitSize, rackId, startUnit, portCount
        );
        EquipmentResponse equipment = equipmentService.createEquipment(req);
        return equipment.id();
    }

    private List<Long> createServers(Long rackId, String baseName, int startServerNum, int count, int unitSize, int portCount) {
        List<Long> ids = new ArrayList<>();
        for (int i = 0; i < count; i++) {
            Long id = createEquipment(rackId,
                    baseName + "-" + (startServerNum + i),
                    EquipmentType.SERVER,
                    unitSize,
                    1, // startUnit بعداً توسط allocateUnits مدیریت می‌شود
                    portCount);
            ids.add(id);
        }
        return ids;
    }


    private Long getFirstPortId(Long equipmentId) {
        // ساده‌ترین راه: اولین پورت تجهیزات
        EquipmentResponse equipment = equipmentService.getEquipmentById(equipmentId);
        return equipment.portIds().get(0);
    }


    private Long getPortId(Long equipmentId, int portNumber) {
        List<Long> portIds = equipmentService.getPortIds(equipmentId);
        return portIds.get(portNumber - 1);
    }

    private void seedSupports(){
        // ایجاد 5 پشتیبان
        for (int i = 1; i <= 5; i++) {
            String username = "support" + i;
            if (userRepository.findByUsername(username).isEmpty()) {
                User support = User.builder()
                        .username(username)
                        .password(passwordEncoder.encode("12345678"))
                        .role(UserRole.SUPPORT)
                        .build();
                userRepository.save(support);
            }
        }
        System.out.println("Support seeding is done");
    }
}