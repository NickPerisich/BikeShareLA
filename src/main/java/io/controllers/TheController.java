package io.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import io.main.BikeStatsApp;

@Controller
public class TheController {
	
	//Sets up a HTTP Get request for index
	@GetMapping("/")
	public String index(Model model) {
		/*
		 * Maps data using Thymeleaf to the HTML page.
		 * The reason for this is due to the fact that data in the CSV could change (we could use a different CSV)
		 * This way, the web app can be somewhat dynamic in the stats it deals with
		 * The attribute names point to locations in HTML tags in the index.html page
		 */
		model.addAttribute("avgtime", BikeStatsApp.data.getAverageTime());
		model.addAttribute("poppasstype", BikeStatsApp.data.getPopPassType());
		model.addAttribute("poptriptype", BikeStatsApp.data.getPopTripType());
		model.addAttribute("popstart", BikeStatsApp.data.getMostPopularStart());
		model.addAttribute("popend", BikeStatsApp.data.getMostPopularEnd());
		model.addAttribute("npopstart", BikeStatsApp.data.getLeastPopularStart());
		model.addAttribute("npopend", BikeStatsApp.data.getLeastPopularEnd());
		model.addAttribute("commute", BikeStatsApp.data.getRegularRiders());
		model.addAttribute("dist", BikeStatsApp.data.getAverageDistance());
		model.addAttribute("wdist", BikeStatsApp.data.getWinterDistance());
		model.addAttribute("sdist", BikeStatsApp.data.getSummerDistance());
		model.addAttribute("wpoptriptype", BikeStatsApp.data.getWinterTripType());
		model.addAttribute("spoptriptype", BikeStatsApp.data.getSummerTripType());
		model.addAttribute("wavgtime", BikeStatsApp.data.getWinterDuration());
		model.addAttribute("savgtime", BikeStatsApp.data.getSummerDuration());
		return "index";
	}
}
