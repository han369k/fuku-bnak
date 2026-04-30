package com.javaeasybank.customer.service;

import com.javaeasybank.common.exception.BusinessException;
import com.javaeasybank.customer.dto.CustomerDto;
import com.javaeasybank.customer.entity.CustomerProfile;
import com.javaeasybank.customer.repository.CustomerProfileRepository;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class CustomerServiceImpl implements CustomerService {

    private final CustomerProfileRepository customerProfileRepository;

    public CustomerServiceImpl(CustomerProfileRepository customerProfileRepository) {
        this.customerProfileRepository = customerProfileRepository;
    }

    // ===========================
    // CRUD
    // ===========================
    @Override
    public List<CustomerDto.CustomerResponse> getAllCustomers() {
        return customerProfileRepository.findAll().stream()
                .map(this::convertToResponse)
                .collect(Collectors.toList());
    }

    @Override
    public List<CustomerDto.CustomerResponse> searchByName(String keyword) {
        return customerProfileRepository.findByNameContaining(keyword).stream()
                .map(this::convertToResponse)
                .collect(Collectors.toList());
    }

    @Override
    public CustomerDto.CustomerResponse createCustomer(CustomerDto.CustomerRequest request) {
        if (customerProfileRepository.findByIdNumber(request.getIdNumber()).isPresent()) {
            throw new BusinessException("身分證字號已存在");
        }

        CustomerProfile profile = new CustomerProfile();
        BeanUtils.copyProperties(request, profile);

        // customer_id: UUID 短碼風格 c-xxxxxxxx
        if (profile.getCustomerId() == null || profile.getCustomerId().isEmpty()) {
            profile.setCustomerId("c-" + UUID.randomUUID().toString().substring(0, 8));
        }

        // cif: C-NNNNNNN-D 格式（7位流水號 + 檢查碼）
        long count = customerProfileRepository.count() + 1;
        String seq = String.format("%07d", count);
        profile.setCif("C-" + seq + "-" + calcCheckDigit(seq));

        profile.setStatus("ACTIVE");

        CustomerProfile saved = customerProfileRepository.save(profile);
        return convertToResponse(saved);
    }

    @Override
    public CustomerDto.CustomerResponse updateCustomer(String customerId, CustomerDto.CustomerRequest request) {
        CustomerProfile profile = customerProfileRepository.findById(customerId)
                .orElseThrow(() -> new BusinessException("查無此客戶"));

        profile.setPhone(request.getPhone());
        profile.setAddress(request.getAddress());
        profile.setEmail(request.getEmail());

        CustomerProfile saved = customerProfileRepository.save(profile);
        return convertToResponse(saved);
    }

    @Override
    public void deactivateCustomer(String customerId) {
        CustomerProfile profile = customerProfileRepository.findById(customerId)
                .orElseThrow(() -> new BusinessException("查無此客戶"));
        profile.setStatus("INACTIVE");
        customerProfileRepository.save(profile);
    }

    // ===========================
    // 一鍵帶入測試資料（20 筆）
    // ===========================
    @Override
    public void seedTestData() {
        if (customerProfileRepository.count() > 0) {
            throw new BusinessException("資料庫已有客戶資料，請先清空再帶入");
        }

        String[][] data = {
            {"A123456789", "王大明", "M", "1985-05-15", "0912000001", "ming.wang@email.com", "台北市信義區信義路五段1號"},
            {"B223456789", "林小華", "F", "1990-08-22", "0912000002", "hua.lin@email.com", "台中市西屯區台灣大道三段99號"},
            {"C123456789", "陳建國", "M", "1978-11-03", "0912000003", "chien.chen@email.com", "高雄市三民區建國一路1號"},
            {"D223456789", "張雅婷", "F", "1995-02-14", "0912000004", "ya.chang@email.com", "台南市東區大學路1號"},
            {"E123456789", "李志明", "M", "1982-07-30", "0912000005", "chih.lee@email.com", "新北市板橋區中山路一段161號"},
            {"F223456789", "吳美玲", "F", "1988-12-10", "0912000006", "mei.wu@email.com", "桃園市中壢區中大路300號"},
            {"G123456789", "黃文輝", "M", "1975-04-05", "0912000007", "wen.huang@email.com", "新竹市東區光復路二段101號"},
            {"H223456789", "蔡佳蓉", "F", "1992-09-18", "0912000008", "chia.tsai@email.com", "苗栗縣苗栗市縣府路100號"},
            {"I123456789", "劉冠宇", "M", "1980-01-25", "0912000009", "kuan.liu@email.com", "彰化縣彰化市中山路二段416號"},
            {"J223456789", "許家瑩", "F", "1998-06-08", "0912000010", "chia.hsu@email.com", "南投縣南投市中興路660號"},
            {"K123456789", "鄭宗翰", "M", "1986-10-12", "0912000011", "tsung.cheng@email.com", "雲林縣斗六市雲林路二段515號"},
            {"L223456789", "洪玉婷", "F", "1991-03-20", "0912000012", "yu.hung@email.com", "嘉義市東區中山路199號"},
            {"M123456789", "邱信宏", "M", "1979-08-05", "0912000013", "hsin.chiu@email.com", "屏東縣屏東市自由路527號"},
            {"N223456789", "曾婉茹", "F", "1994-11-28", "0912000014", "wan.tseng@email.com", "宜蘭縣宜蘭市縣政北路1號"},
            {"O123456789", "廖偉翔", "M", "1983-05-10", "0912000015", "wei.liao@email.com", "花蓮縣花蓮市府前路17號"},
            {"P223456789", "賴怡君", "F", "1987-12-01", "0912000016", "yi.lai@email.com", "台東縣台東市中山路276號"},
            {"Q123456789", "徐俊傑", "M", "1976-02-18", "0912000017", "chun.hsu@email.com", "澎湖縣馬公市治平路32號"},
            {"R223456789", "卓佩樺", "F", "1996-07-07", "0912000018", "pei.cho@email.com", "基隆市中正區義一路1號"},
            {"S123456789", "江宇軒", "M", "1989-09-30", "0912000019", "yu.chiang@email.com", "台北市中正區重慶南路一段122號"},
            {"T223456789", "郭欣儀", "F", "1993-04-25", "0912000020", "hsin.kuo@email.com", "新北市三重區重新路一段1號"},
        };

        for (int i = 0; i < data.length; i++) {
            String[] d = data[i];
            CustomerProfile p = new CustomerProfile();
            p.setCustomerId("c-" + UUID.randomUUID().toString().substring(0, 8));
            String seq = String.format("%07d", i + 1);
            p.setCif("C-" + seq + "-" + calcCheckDigit(seq));
            p.setIdNumber(d[0]);
            p.setName(d[1]);
            p.setGender(d[2]);
            p.setBirthday(LocalDate.parse(d[3]));
            p.setPhone(d[4]);
            p.setEmail(d[5]);
            p.setAddress(d[6]);
            p.setStatus("ACTIVE");
            customerProfileRepository.save(p);
        }
    }

    // ===========================
    // 給其他模組對接用
    // ===========================
    @Override
    public CustomerDto.CustomerResponse findByCustomerId(String customerId) {
        CustomerProfile profile = customerProfileRepository.findById(customerId)
                .orElseThrow(() -> new BusinessException("查無此客戶"));
        return convertToResponse(profile);
    }

    @Override
    public CustomerDto.CustomerResponse findByIdNumber(String idNumber) {
        CustomerProfile profile = customerProfileRepository.findByIdNumber(idNumber)
                .orElseThrow(() -> new BusinessException("查無此客戶"));
        return convertToResponse(profile);
    }

    @Override
    public CustomerDto.CustomerResponse findByCif(String cif) {
        CustomerProfile profile = customerProfileRepository.findByCif(cif)
                .orElseThrow(() -> new BusinessException("查無此客戶編號：" + cif));
        return convertToResponse(profile);
    }

    // ===========================
    // 私有方法
    // ===========================
    private CustomerDto.CustomerResponse convertToResponse(CustomerProfile profile) {
        CustomerDto.CustomerResponse res = new CustomerDto.CustomerResponse();
        BeanUtils.copyProperties(profile, res);
        return res;
    }

    private int calcCheckDigit(String seq) {
        int sum = 0;
        for (char c : seq.toCharArray()) {
            sum += Character.getNumericValue(c);
        }
        return sum % 10;
    }
}
