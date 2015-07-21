package games;

import java.awt.image.BufferedImage;

public class GameImage extends LoadImage {
	private static final int TOTAL_THEMES = 10;
	private static final int TOTAL_BLOCKS = 7;
	private static final int BKGD_TOTAL = 15;

	private BufferedImage[][] blockImage;
	private BufferedImage[] backgroundImage;
	private BufferedImage controlKeyImage;
	
	private BufferedImage endGameImage;
	private BufferedImage soundOnImage;
	private BufferedImage soundOffImage;
	private BufferedImage escKeyImage;

	private BufferedImage logoTextLevel;
	private BufferedImage logoTextLines;
	private BufferedImage logoTextNext;
	private BufferedImage logoTextScore;

	private int bkgdTheme;
	private int countTheme;

	public GameImage() {
		setGameImage();
	}

	public void setGameImage() {
		bkgdTheme = 0;
		countTheme = TOTAL_THEMES;
		blockImage = new BufferedImage[TOTAL_THEMES][TOTAL_BLOCKS];
		backgroundImage = new BufferedImage[BKGD_TOTAL];
		loadGameImage();
	}

	public boolean isMaxTheme() {
		return (getBlockTheme() == TOTAL_THEMES) ? true:false;
	}

	public void changeBlockTheme() {
		countTheme = (countTheme >= TOTAL_THEMES) ? 0: ++ countTheme;
	}

	public void changeBkgdTheme() {
		bkgdTheme = (bkgdTheme >= backgroundImage.length - 1) ? 0 : ++bkgdTheme;
	}

	public int getBlockTheme() {
		return countTheme;
	}

	public int getBkgdTheme() {
		return bkgdTheme;
	}

	public BufferedImage[] getBlockImage() {
		return blockImage[countTheme];
	}

	public BufferedImage getBackgroundImage() {
		return backgroundImage[bkgdTheme];
	}

	public BufferedImage getEndGameImage() {
		return endGameImage;
	}

	public BufferedImage getSoundOnImage() {
		return soundOnImage;
	}

	public BufferedImage getSoundOffImage() {
		return soundOffImage;
	}

	public BufferedImage getControlKeyImage() {
		return controlKeyImage;
	}

	public BufferedImage getEscKeyImage() {
		return escKeyImage;
	}

	public BufferedImage getLogoTextLevel() {
		return logoTextLevel;
	}

	public BufferedImage getLogoTextLines() {
		return logoTextLines;
	}

	public BufferedImage getLogoTextNext() {
		return logoTextNext;
	}

	public BufferedImage getLogoTextScore() {
		return logoTextScore;
	}

	private void loadGameImage() {
		String[][] pic = {

				{ "block_bold_cyan.png", "block_bold_yellow.png",
						"block_bold_blue.png", "block_bold_lightgreen.png",
						"block_bold_darkblue.png", "block_bold_green.png",
						"block_bold_red.png" },

				{ "block_pushbutton_blue.png", "block_pushbutton_green.png",
						"block_pushbutton_red.png",
						"block_pushbutton_purple.png",
						"block_pushbutton_orange.png",
						"block_pushbutton_yellow.png",
						"block_pushbutton_cyan.png" },

				{ "iron_blue_block.png", "iron_darkblue_block.png",
						"iron_green_block.png", "iron_orange_block.png",
						"iron_purple_block.png", "iron_red_block.png",
						"iron_yellow_block.png" },

				{ "block_metal_blue.png", "block_metal_green.png",
						"block_metal_red.png", "block_metal_purple.png",
						"block_metal_orange.png", "block_metal_yellow.png",
						"block_metal_cyan.png" },

				{ "block_gel_blue.png", "block_gel_green.png",
						"block_gel_red.png", "block_gel_purple.png",
						"block_gel_orange.png", "block_gel_yellow.png",
						"block_gel_cyan.png" },

				{ "emoji_blue.png", "emoji_darkblue.png", "emoji_green.png",
						"emoji_pink.png", "emoji_purple.png", "emoji_red.png",
						"emoji_yellow.png" },

				{ "block_mushroom_blue02.png", "block_mushroom_brown.png",
						"block_mushroom_green.png",
						"block_mushroom_orange.png",
						"block_mushroom_purple.png",
						"block_mushroom_red02.png",
						"block_mushroom_lightblue.png" },

				{ "block_button_blue.png", "block_button_green.png",
						"block_button_red.png", "block_button_purple.png",
						"block_button_orange.png", "block_button_yellow.png",
						"block_button_cyan.png" },

				{ "block_dragonball_01.png", "block_dragonball_02.png",
						"block_dragonball_03.png", "block_dragonball_04.png",
						"block_dragonball_05.png", "block_dragonball_06.png",
						"block_dragonball_07.png" },

				{ "block_ball_baseball.png", "block_ball_baskitball.png",
						"block_ball_beach.png", "block_ball_football.png",
						"block_ball_soccer.png", "block_ball_volley.png",
						"block_ball_yellow_soccer.png" } };
		//
		// load brick images
		//
		for (int k = 0; k < TOTAL_THEMES; k++)
			for (int i = 0; i < 7; i++)
				blockImage[k][i] = getImage(pic[k][i]);

		String[] bkgdImage = { "bkgd_building.jpg", "bkgd_gravel.jpg",
				"bkgd_food.jpg", "bkgd_live.jpg", "bkgd_wave.jpg",
				"bkgd_playgame.jpg", "bkgd_crack.jpg", "bkgd_mario.jpg",
				"bkgd_colors.jpg", "bkgd_rockandsky.jpg",
				"bkgd_grey_bricks.jpg", "bkgd_earth.jpg",
				"bkgd_darkred_bricks.jpg", "bkgd_jungle.jpg",
				"bkgd_black_bricks.jpg" };

		//
		// load background wallpaper
		//
		for (int i = 0; i < BKGD_TOTAL; i++)
			backgroundImage[i] = getImage(bkgdImage[i]);
		//
		// load message images
		//
		endGameImage = getImage("logo_endgame.png");
		escKeyImage = getImage("logo_press_esc.png");
		controlKeyImage = getImage("logo_control_keys.png");
		soundOnImage = getImage("logo_sound_on.png");
		soundOffImage = getImage("logo_sound_off.png");

		//
		// load logo images
		//
		logoTextLevel = getImage("logo_text_level.png");
		logoTextLines = getImage("logo_text_lines.png");
		logoTextNext = getImage("logo_text_next.png");
		logoTextScore = getImage("logo_text_score.png");

	}

}
