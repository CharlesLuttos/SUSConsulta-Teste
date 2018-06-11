package com.dampcake.robotest.unitarios;

import android.view.MenuItem;

import com.dampcake.robotest.DetalhesConsultasActivity;
import com.dampcake.robotest.R;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.fakes.RoboMenuItem;

import static org.junit.Assert.assertTrue;

/**
 * Created by Pessoa on 23/05/2018.
 */

@RunWith(RobolectricTestRunner.class)

public class DetalhesConsultasActivityTest {
    @Test
    public void testCreate() {
        DetalhesConsultasActivity activity = Robolectric.buildActivity(DetalhesConsultasActivity.class).create().get();
        boolean result;
        result = activity != null;
        assertTrue(result);
    }

    @Test
    public void selecionarItemMenuOpcoes() {
        DetalhesConsultasActivity activity = Robolectric.buildActivity(DetalhesConsultasActivity.class).create().visible().get();
        MenuItem menuItem = new RoboMenuItem(R.id.action_settings);
        assertTrue(activity.onOptionsItemSelected(menuItem));
    }
}
