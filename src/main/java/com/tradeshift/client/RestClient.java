package com.tradeshift.client;

import com.sun.jersey.api.client.WebResource;

public interface RestClient {
    /**
     * Gets a jersey WebResource. The user can configure it for sub-paths, and then pass it to one of the other methods of this interface.
     */
    public WebResource resource();

    /**
     * POST Does a http POST on the given resource.
     */
    public void post(WebResource resource);
}
