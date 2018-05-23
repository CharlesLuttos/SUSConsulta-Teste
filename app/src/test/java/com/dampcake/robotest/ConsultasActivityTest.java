package com.dampcake.robotest;

import android.content.Intent;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.shadows.ShadowApplication;

import static org.junit.Assert.assertEquals;

@RunWith(RobolectricTestRunner.class)
public class ConsultasActivityTest {

    @Test
    public void abrirCadastroConsultaActivity(){
        ConsultasActivity activity = Robolectric.buildActivity(ConsultasActivity.class).create().visible().get();
        activity.findViewById(R.id.fab).performClick();
        Intent expectedIntent = new Intent(activity, CadastroConsultasActivity.class);
        Intent actual = ShadowApplication.getInstance().getNextStartedActivity();
        assertEquals(expectedIntent.getComponent(), actual.getComponent());
    }
}
