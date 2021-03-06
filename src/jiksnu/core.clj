(ns jiksnu.core
  (:require [jiksnu.db :as db]
            ;; jiksnu.factory
            ;; jiksnu.formats
            jiksnu.modules.core.triggers.conversation-triggers
            jiksnu.modules.core.triggers.domain-triggers
            jiksnu.workers
            [taoensso.timbre :as timbre]))

(defn start
  []
  (timbre/info "starting core")
  (db/set-database!))
