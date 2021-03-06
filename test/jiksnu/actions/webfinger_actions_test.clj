(ns jiksnu.actions.webfinger-actions-test
  (:require [ciste.config :refer [config]]
            [clj-factory.core :refer [fseq]]
            [jiksnu.actions.webfinger-actions :as actions.webfinger]
            jiksnu.factory
            [jiksnu.mock :as mock]
            [jiksnu.model.user :as model.user]
            [jiksnu.model.webfinger :as model.webfinger]
            [jiksnu.ops :as ops]
            [jiksnu.test-helper :as th]
            [midje.sweet :refer [=> after before fact future-fact
                                 namespace-state-changes truthy throws]])
  (:import nu.xom.Document))

(namespace-state-changes
 [(before :contents (th/setup-testing))
  (after :contents (th/stop-testing))])

(fact "#'actions.webfinger/fetch-host-meta"
  (let [resource (mock/a-resource-exists)
        url (:_id resource)]
    (fact "when the url is nil"
      (actions.webfinger/fetch-host-meta nil) => (throws AssertionError))
    (fact "when the url points to a valid XRD document"
      (actions.webfinger/fetch-host-meta url) => (partial instance? Document)
      (provided
        (ops/update-resource url) => (ref {:status 200
                                       :body "<XRD/>"})))
    (fact "when the url does not point to a valid XRD document"
      (actions.webfinger/fetch-host-meta url) => (throws RuntimeException)
      (provided
        (ops/update-resource url) => (ref {:status 404
                                           :body "<html><body><p>Not Found</p></body></html>"})))))

(fact "#'actions.webfinger/host-meta"
  (let [domain (config :domain)
        response (actions.webfinger/host-meta)]
    response => map?
    (:host response) => domain
    (count (:links response)) => (partial >= 1)))
