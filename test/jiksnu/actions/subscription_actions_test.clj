(ns jiksnu.actions.subscription-actions-test
  (:require [clj-factory.core :refer [factory fseq]]
            [jiksnu.actions.subscription-actions :as actions.subscription]
            [jiksnu.mock :as mock]
            [jiksnu.model :as model]
            [jiksnu.model.subscription :as model.subscription]
            [jiksnu.model.user :as model.user]
            [jiksnu.ops :as ops]
            [jiksnu.session :as session]
            [jiksnu.test-helper :as th]
            [midje.sweet :refer :all])
  (:import jiksnu.model.Subscription
           jiksnu.model.User))


(namespace-state-changes
 [(before :contents (th/setup-testing))
  (after :contents (th/stop-testing))])

(fact "#'actions.subscription/subscribe"
  (fact "when the user is not already subscribed"
    (model.subscription/drop!)
    (let [user (mock/a-user-exists)
          subscribee (mock/a-user-exists)]
      (session/with-user user
        (actions.subscription/subscribe user subscribee)) =>
        (partial instance? Subscription))))

(fact "#'actions.subscription/ostatussub-submit"
  (let [actor (mock/a-user-exists)
        username (fseq :username)
        domain-name (fseq :domain)
        uri (model.user/get-uri {:username username :domain domain-name})]
    (session/with-user actor
      (actions.subscription/ostatussub-submit uri)) =>
    (every-checker
     (partial instance? Subscription)
     (contains {:from (:_id actor)
                :to uri}))))

(fact "#'actions.subscription/subscribed"
  (let [user (mock/a-user-exists)
        subscribee (mock/a-user-exists)]
    (actions.subscription/subscribed user subscribee) =>
    (partial instance? Subscription)))

(fact "#'actions.subscription/get-subscribers"
  (fact "when there are subscribers"
    (let [subscription (mock/a-subscription-exists)
          target (model.subscription/get-target subscription)]
      (actions.subscription/get-subscribers target) =>
      (just
       (partial instance? User)
       (contains
        {:totalItems pos?
         :items (every-checker
                 (has every? (partial instance? Subscription))
                 (has some (partial = subscription)))})))))

(fact "#'actions.subscription/get-subscriptions"
  (fact "when there are subscriptions"
    (let [subscription (mock/a-subscription-exists)
          actor (model.subscription/get-actor subscription)]
      (actions.subscription/get-subscriptions actor) =>
      (just
       (partial = actor)
       (contains
        {:totalItems pos?
         :items
         (every-checker
          (has every? (partial instance? Subscription))
          (has some (partial = subscription)))})))))
