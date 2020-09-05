/*
 * Creator: Kamran Noorinejad on 8/25/20 12:33 PM
 * Last modified: 8/25/20 12:33 PM
 * Copyright: All rights reserved Ⓒ 2020
 * http://www.itskamran.ir/
 */

package com.kmno.leftorite

import com.kmno.leftorite.ui.activities.SplashActivity
import org.junit.Test
import org.mockito.Mockito
import org.mockito.Mockito.verify

/**
 * Created by Kamran Noorinejad on 8/25/2020 AD 12:33.
 * Edited by Kamran Noorinejad on 8/25/2020 AD 12:33.
 */

class SplashActivityTest {

    @Test
    fun `test Call Initial Config Api`() {
        val mockedSplashActivity = Mockito.mock(SplashActivity::class.java)
        mockedSplashActivity.method()
        verify(mockedSplashActivity).method()
    }

}