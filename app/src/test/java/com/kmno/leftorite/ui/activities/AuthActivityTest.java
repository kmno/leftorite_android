/*
 * Creator: Kamran Noorinejad on 9/7/20 3:01 PM
 * Last modified: 9/7/20 3:01 PM
 * Copyright: All rights reserved Ⓒ 2020
 * http://www.itskamran.ir/
 */

package com.kmno.leftorite.ui.activities;

import android.content.BroadcastReceiver;
import android.content.res.Resources;

import androidx.appcompat.app.AppCompatDelegate;
import androidx.collection.SimpleArrayMap;
import androidx.collection.SparseArrayCompat;
import androidx.core.app.ComponentActivity;
import androidx.fragment.app.FragmentController;
import androidx.lifecycle.LifecycleRegistry;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelStore;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import kotlin.Lazy;

/**
 * Created by Kamran Noorinejad on 9/7/2020 AD 15:01.
 * Edited by Kamran Noorinejad on 9/7/2020 AD 15:01.
 */
public class AuthActivityTest {
    @Mock
    Lazy authActivityViewModel$delegate;
    @Mock
    BroadcastReceiver broadcastReceiver;
    @Mock
    AppCompatDelegate mDelegate;
    @Mock
    Resources mResources;
    @Mock
    FragmentController mFragments;
    @Mock
    LifecycleRegistry mFragmentLifecycleRegistry;
    @Mock
    SparseArrayCompat<String> mPendingFragmentActivityResults;
    @Mock
    LifecycleRegistry mLifecycleRegistry;
    //Field mSavedStateRegistryController of type SavedStateRegistryController - was not mocked since Mockito doesn't mock a Final class when 'mock-maker-inline' option is not set
    @Mock
    ViewModelStore mViewModelStore;
    @Mock
    ViewModelProvider.Factory mDefaultFactory;
    //Field mOnBackPressedDispatcher of type OnBackPressedDispatcher - was not mocked since Mockito doesn't mock a Final class when 'mock-maker-inline' option is not set
    @Mock
    SimpleArrayMap<Class<? extends ComponentActivity.ExtraData>, ComponentActivity.ExtraData> mExtraDataMap;
    @InjectMocks
    AuthActivity authActivity;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testGetResId() throws Exception {
        int result = authActivity.getResId();
        Assert.assertEquals(0, result);
    }

    @Test
    public void testAfterCreate() throws Exception {
        authActivity.afterCreate();
    }

    @Test
    public void testReady() throws Exception {
        authActivity.ready();
    }

    @Test
    public void testResume() throws Exception {
        authActivity.resume();
    }

    @Test
    public void testPause() throws Exception {
        authActivity.pause();
    }

    @Test
    public void testDestroy() throws Exception {
        authActivity.destroy();
    }

    @Test
    public void testNetworkStatus() throws Exception {
        authActivity.networkStatus(true);
    }

    @Test
    public void testHandleNetworkErrors() throws Exception {
        authActivity.handleNetworkErrors("errorMessage", null);
    }

    @Test
    public void testOnBackPressed() throws Exception {
        authActivity.onBackPressed();
    }

    @Test
    public void testOnNetConnected() throws Exception {
        authActivity.onNetConnected();
    }

    @Test
    public void testOnNetDisConnected() throws Exception {
        authActivity.onNetDisConnected();
    }

    @Test
    public void testSetUpScreen() throws Exception {
        authActivity.setUpScreen();
    }

    @Test
    public void testSetStatusBarTextColorWhite() throws Exception {
        authActivity.setStatusBarTextColorWhite();
    }
}

//Generated with love by TestMe :) Please report issues and submit feature requests at: http://weirddev.com/forum#!/testme