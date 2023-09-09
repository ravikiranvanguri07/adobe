package nanisite.core.servlets;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.jcr.Session;
import javax.servlet.Servlet;

import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.SlingHttpServletResponse;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.api.servlets.SlingAllMethodsServlet;
import org.apache.sling.commons.json.JSONObject;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.day.cq.search.PredicateGroup;
import com.day.cq.search.Query;
import com.day.cq.search.QueryBuilder;

import nanisite.core.utility.SiteConstants;

import com.day.cq.search.result.Hit;
import com.day.cq.search.result.SearchResult;
import com.day.cq.wcm.api.Page;

@Component(service = { Servlet.class }, property = { "sling.servlet.methods= GET",
		"sling.servlet.paths=" + "/bin/nanisite/sitesearch" })
public class SiteSearchBar extends SlingAllMethodsServlet {
	private final static Logger log = LoggerFactory.getLogger(SiteSearchBar.class);

	@Reference
	QueryBuilder builder;

	@Override
	protected void doGet(SlingHttpServletRequest request, SlingHttpServletResponse response) {
		String keyword = request.getParameter(SiteConstants.SEARCH_KEYWORD);
		log.info("keyword : {}", keyword);
		JSONObject jsonSearchObject = new JSONObject();
		ResourceResolver resourceResolver = request.getResourceResolver();
		if (keyword != null) {
			Map<String, String> map = new HashMap<String, String>();
			map.put(SiteConstants.PAGE_PATH, SiteConstants.MYSITE_PATH);
			map.put(SiteConstants.TYPE, "cq:Page");
			map.put(SiteConstants.FULLTEXT, keyword);
			map.put("p.limit", "-1");
			log.info("Map : {}", map.toString());
			Session session = resourceResolver.adaptTo(Session.class);

			try {
				if (builder != null) {
					Query query = builder.createQuery(PredicateGroup.create(map), session);
					if (query != null) {
						SearchResult searchResult = query.getResult();
						log.info("searchResult : {}", searchResult);
						log.info("searchResult : {} {}", searchResult.getHits().toString(),
								searchResult.getHits().isEmpty());
						if (searchResult.getHits().isEmpty()) {
							response.getWriter().write("No Results");
						} else {
							int index = 0;
							for (Hit hitsList : searchResult.getHits()) {
								Resource hitsresource = hitsList.getResource();
								log.info("Hits Resource: {}", hitsresource);
								if (hitsresource != null) {
									Page page = hitsresource.adaptTo(Page.class);
									if (page != null) {
										JSONObject obj = new JSONObject();
										obj.put(SiteConstants.PAGE_TITLE, page.getTitle());

										obj.put(SiteConstants.PAGE_PATH, page.getPath());
										obj.put(SiteConstants.DESCRIPTION, page.getDescription());
										log.info(" Page Results : {}", obj.toString());

										jsonSearchObject.put("page_results" + (index++), obj);

									}
								}
							}
						}
					}
					log.info("Response : {}", jsonSearchObject.toString());
					response.getWriter().write(jsonSearchObject.toString());
				}
			} catch (Exception e) {
				log.error("Exception : {}", e);
				e.printStackTrace();
			}

		}

	}

}
