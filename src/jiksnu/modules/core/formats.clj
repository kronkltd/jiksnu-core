(ns jiksnu.modules.core.formats
  (:require [ciste.core :refer [with-format]]
            [ciste.formats :refer [format-as]]
            [clojure.data.json :as json]
            [hiccup.core :as h]))

(defmethod format-as :as
  [format request response]
  (with-format :json (format-as :json request response)))

;; (defmethod format-as :default
;;   [format request response]
;;   response)

(defmethod format-as :page
  [format request response]
  response)

(defmethod format-as :model
  [format request response]
  (with-format :json
    (doall (format-as :json request response))))
