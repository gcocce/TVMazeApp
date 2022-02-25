package com.example.tvmazeapp.utils

import org.junit.Assert
import org.junit.Test
import com.example.tvmazeapp.utils.Security as Security


class SecurityEncryptionTest(){

    @Test
    fun testEncryption() {

        for (i in 0..1000){
            val stringTest = UtilTest.getRandomString(10)

            val encryptedPass = Security.md5(stringTest)

            Assert.assertEquals(encryptedPass, Security.md5(stringTest))
        }
    }
}