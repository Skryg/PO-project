package com.poproject.game.screen.ui;

import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.poproject.game.Assets;
import com.poproject.game.ProjectGame;

public class PauseScreen extends UIScreen {
    public PauseScreen(ProjectGame projectGame) {
        super(projectGame);
    }

    @Override
    public void show() {
        super.show();
        Skin skin = projectGame.getAssetManager().get(Assets.skinUI);

        TextButton continueButton = createContinueButton(skin);
        TextButton preferences = createPreferencesButton(skin);
        TextButton exit = createExitMenuButton(skin);

        table.add(continueButton).fillX().uniformX();
        table.row().pad(10, 0, 10, 0);
        table.add(preferences).fillX().uniformX();
        table.row();
        table.add(exit).fillX().uniformX();
    }
}
