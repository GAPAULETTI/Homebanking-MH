package com.mindhub.homebanking;


import com.mindhub.homebanking.Utils.Util;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import static org.hamcrest.MatcherAssert.assertThat;

import static org.hamcrest.Matchers.*;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class CardUtilsTest {
    /*
    @Test
    public void cardNumberIsCreated(){
        String cardNumber = Util.generateNumberCard();
        assertThat(cardNumber,is(not(emptyOrNullString())));
        assertThat(cardNumber, instanceOf(String.class));
    }
    @Test
    public void cvvIsCreated(){
        int cvvNumber = Util.cvvNumber();
        assertThat(cvvNumber, greaterThan(000));
        assertThat(cvvNumber, isA(Integer.class));
        assertThat(cvvNumber, is(not(nullValue())));
    }
    @Test
    public  void generateNumberAccount(){
        String numberAccount = Util.generateNumberAccount();
        assertThat(numberAccount,is(not(emptyOrNullString())));
    }


     */
}
