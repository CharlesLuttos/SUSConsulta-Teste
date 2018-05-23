package com.dampcake.robotest;

import android.content.Intent;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;
import org.robolectric.shadows.ShadowApplication;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

@RunWith(RobolectricTestRunner.class)
@Config(constants = BuildConfig.class, sdk = 27)
public class UsuarioActivityTest {
    @Test
    public void abrirCadastroUsuarioActivity() {
        UsuarioActivity activity = Robolectric.buildActivity(UsuarioActivity.class).create().visible().get();
        activity.findViewById(R.id.fab).performClick();
        Intent expectedIntent = new Intent(activity, CadastroUsuarioActivity.class);
        Intent actual = ShadowApplication.getInstance().getNextStartedActivity();
        assertEquals(expectedIntent.getComponent(), actual.getComponent());
    }

    @Test
    public void testOnResume() {
        UsuarioActivity activity = Robolectric.buildActivity(UsuarioActivity.class).create().resume().get();
        assertTrue(activity!=null);
    }

}
