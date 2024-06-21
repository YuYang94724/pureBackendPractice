package com.yuyang.pureBackendPractice;

import com.yuyang.pureBackendPractice.wallet.data.dto.WalletOpResponseDTO;
import com.yuyang.pureBackendPractice.wallet.data.dto.WalletResponseDTO;
import com.yuyang.pureBackendPractice.wallet.data.vo.WalletVO;
import com.yuyang.pureBackendPractice.wallet.service.WalletService;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

@SpringBootTest
@Slf4j
@AutoConfigureMockMvc
public class WalletTests {
    @Autowired
    private WalletService walletService;
    //測試controller打http
    @Autowired
    private MockMvc mockMvc;
    @Mock
    private WalletService mockWalletService;
    @Test
    public void query(){
        WalletResponseDTO result = walletService.query("test");
        Assertions.assertNotNull(result);
        Assertions.assertEquals(result.getMemberId(), 2);
        Assertions.assertEquals(result.getBalance(), new BigDecimal(15000));
    }
    @Test
    @Transactional(isolation = Isolation.SERIALIZABLE)
    public void deposit(){
        WalletOpResponseDTO result = walletService.deposit("test", new BigDecimal(2000));
        WalletResponseDTO test = walletService.query("test");
        Assertions.assertEquals(result.getToMemberBalance(), new BigDecimal(17000));
        Assertions.assertEquals(result.getToMemberBalance(), test.getBalance());
    }
    @Test
    @Transactional(isolation = Isolation.SERIALIZABLE)
    public void withdraw(){
        WalletOpResponseDTO result = walletService.withdraw("test", new BigDecimal(5000));
        WalletResponseDTO test = walletService.query("test");
        Assertions.assertEquals(result.getToMemberBalance(), new BigDecimal(10000));
        Assertions.assertEquals(result.getToMemberBalance(), test.getBalance());
    }
    @Test
    @Transactional
    public void queryTransactions() throws Exception {
        Page<WalletVO> walletVOPage = walletService.queryTransactions("test",0, 10);
        Assertions.assertNotNull(walletVOPage);
        Assertions.assertEquals(walletVOPage.getTotalElements(), 9);
        Assertions.assertEquals(walletVOPage.getTotalPages(), 1);
    }
    @Test
    public void transfer(){
        WalletOpResponseDTO walletOpResponseDTO = new WalletOpResponseDTO();
        walletOpResponseDTO.setFromMemberBalance(new BigDecimal(20000));
        walletOpResponseDTO.setFromMemberId(2L);
        walletOpResponseDTO.setToMemberId(1L);
        walletOpResponseDTO.setToMemberBalance(new BigDecimal(20000));
        Mockito.when(mockWalletService.transfer("test", 1L, new BigDecimal(10000)))
                .thenReturn(walletOpResponseDTO);

        WalletOpResponseDTO test = mockWalletService.transfer("test", 1L, new BigDecimal(10000));
        Assertions.assertNotNull(test);
        Assertions.assertEquals(test.getToMemberId(), 1L);
    }

}
