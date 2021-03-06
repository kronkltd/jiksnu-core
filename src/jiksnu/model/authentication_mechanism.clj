(ns jiksnu.model.authentication-mechanism
  (:require [jiksnu.db :refer [_db]]
            [jiksnu.model :as model]
            [jiksnu.templates.model :as templates.model]
            [jiksnu.util :as util]
            [jiksnu.validators :refer [type-of]]
            [monger.core :as mg]
            [monger.collection :as mc]
            [validateur.validation :refer [acceptance-of
                                           presence-of
                                           valid?
                                           validation-set]])
  (:import jiksnu.model.AuthenticationMechanism
           org.bson.types.ObjectId))

(def collection-name "authentication_mechanisms")

(def set-field! (templates.model/make-set-field! collection-name))

(def create-validators
  (validation-set
   ;; (type-of :_id        ObjectId)
   ;; ;; (type-of :created    DateTime)
   ;; ;; (type-of :updated    DateTime)
   ;; ;; (type-of :local      Boolean)
   ;; ;; (type-of :discovered Boolean)
   ))

(defn fetch-by-id
  [id]
  (let [id (if (string? id) (util/make-id id) id)]
    (if-let [item (mc/find-map-by-id @_db collection-name id)]
      (model/map->AuthenticationMechanism item))))

(def create        (templates.model/make-create collection-name #'fetch-by-id #'create-validators))

(defn fetch-all
  ([] (fetch-all {}))
  ([params] (fetch-all params {}))
  ([params options]
   (map model/map->AuthenticationMechanism
        (mc/find-maps @_db collection-name params))))

(defn fetch-by-user
  [user & options]
  (apply fetch-all {:user (:_id user)} options))

(def delete        (templates.model/make-deleter       collection-name))
(def drop!         (templates.model/make-dropper       collection-name))
(def count-records (templates.model/make-counter       collection-name))
(def remove-field! (templates.model/make-remove-field! collection-name))
