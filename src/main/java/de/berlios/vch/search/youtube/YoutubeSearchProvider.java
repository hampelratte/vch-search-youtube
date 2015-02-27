package de.berlios.vch.search.youtube;

import java.net.URI;
import java.net.URLEncoder;

import org.apache.felix.ipojo.annotations.Component;
import org.apache.felix.ipojo.annotations.Provides;
import org.apache.felix.ipojo.annotations.Requires;
import org.osgi.framework.ServiceException;

import de.berlios.vch.parser.IOverviewPage;
import de.berlios.vch.parser.IWebPage;
import de.berlios.vch.parser.IWebParser;
import de.berlios.vch.parser.OverviewPage;
import de.berlios.vch.search.ISearchProvider;

@Component
@Provides
public class YoutubeSearchProvider implements ISearchProvider {

    @Requires(filter = "(instance.name=vch.parser.youtube)")
    private IWebParser youtubeParser;

    @Override
    public String getName() {
        return youtubeParser.getTitle();
    }

    @Override
    public IOverviewPage search(String query) throws Exception {
        if (youtubeParser == null) {
            throw new ServiceException("Youtube Parser is not available");
        }

        String q = URLEncoder.encode(query, "UTF-8");
        String uri = "https://www.googleapis.com/youtube/v3/search?part=snippet&maxResults=30&q=" + q;
        IOverviewPage opage = new OverviewPage();
        opage.setParser(getId());
        opage.setUri(new URI(uri));
        opage = (IOverviewPage) youtubeParser.parse(opage);
        return opage;
    }

    @Override
    public String getId() {
        return youtubeParser.getId();
    }

    @Override
    public IWebPage parse(IWebPage page) throws Exception {
        if (youtubeParser == null) {
            throw new ServiceException("Youtube Parser is not available");
        }
        return youtubeParser.parse(page);
    }
}
