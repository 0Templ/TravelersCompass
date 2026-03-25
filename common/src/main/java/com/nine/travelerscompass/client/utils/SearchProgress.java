package com.nine.travelerscompass.client.utils;

import net.minecraft.util.Mth;

public class SearchProgress {
	
	public int progress;
	
	public int total;
	
	public SearchProgress(int p, int t) {
		this.progress = p;
		this.total = t;
	}
	
	public void update(int p, int t) {
		this.progress = p;
		this.total = t;
	}

	public boolean isValid() {
		return progress >= 0 && total > 0;
	}

	public float fraction() {
		if (!isValid()) {
			return 0.0F;
		}
		return Mth.clamp((float) progress / (float) total, 0.0F, 1.0F);
	}

	public int percent() {
		return Mth.clamp(Mth.floor(fraction() * 100.0F), 0, 100);
	}
	
}
