package com.dampcake.robotest.unitarios;

import android.content.Intent;

import com.dampcake.robotest.BuildConfig;
import com.dampcake.robotest.CadastroUsuarioActivity;
import com.dampcake.robotest.R;
import com.dampcake.robotest.UsuarioActivity;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;
import org.robolectric.shadows.ShadowApplication;

import static org.junit.Assert.assertEquals;

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
    public void testeCliqueListaUsuarios() {
        UsuarioActivity activity = Robolectric.buildActivity(UsuarioActivity.class).create().visible().get();
        activity.listViewUsuarios.performItemClick(activity.getCurrentFocus(), 0,1);
    }

    @Test
    public void testeCliqueLongoListaUsuarios() {
        UsuarioActivity activity = Robolectric.buildActivity(UsuarioActivity.class).create().visible().get();
        activity.listViewUsuarios.performLongClick();
    }

    @Test
    public void testeExibitToast() {
        UsuarioActivity activity = Robolectric.buildActivity(UsuarioActivity.class).create().visible().get();
        activity.exibirToast("Ok");
    }

    @Test
    public void testeCriarMenuDeContexto() {
        UsuarioActivity activity = Robolectric.buildActivity(UsuarioActivity.class).create().visible().get();
    }
}
