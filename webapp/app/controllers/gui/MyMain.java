package controllers.gui;

import gov.nrel.util.Utility;

import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import controllers.gui.auth.GuiSecure;
import controllers.gui.util.Chart;
import controllers.gui.util.ChartInfo;
import controllers.gui.util.ChartUtil;

import play.mvc.Controller;
import play.mvc.With;

import models.ChartDbo;
import models.DashboardSettings;
import models.EntityUser;
import models.UserSettings;

@With(GuiSecure.class)
public class MyMain extends Controller {

	public static void dashboard() {
		EntityUser user = Utility.getCurrentUser(session);
		List<ChartDbo> charts = user.getCharts();
		String title = "dddSummary";
		render(user, charts, title);
	}
	
	public static void single(String chartId, String encoded) {
		EntityUser user = Utility.getCurrentUser(session);
		List<ChartDbo> charts = user.getCharts();

		//special case for showing remote pages
		if(Settings.EMBEDDED_CHART_ID.equals(chartId)) {
			Map<String, String> variables = ChartUtil.decodeVariables(encoded);
			String url = variables.get("url");
			String title = variables.get("title");
			render(charts, url, title);
		} else if(ChartInfo.BUILT_IN_CHART1.equals(chartId)) {
			ChartDbo chart = new ChartDbo();
			Chart theChart = MyCharts.deserialize(encoded);
			String title = theChart.getTitle();
			chart.setChartId(chartId);
			chart.setEncodedVariablesRaw(encoded);
			chart.setTitle(title);
			String url = chart.getLargeChartUrl();
			render(charts, url, title);			
		} else {
			ChartDbo chart = new ChartDbo();
			chart.setChartId(chartId);
			chart.setEncodedVariables(encoded);
			String title = chart.getTitle();
			String url = chart.getLargeChartUrl();
			render(charts, url, title);
		}
	}
}
