(ns recepta-ante.model.reparto
  (:require
   [honeysql.helpers :as hh]
   [recepta-ante.services.database :as db]))

(defn select-id
  [datasource reparto-codice presidio-id]
  (let [q (-> (hh/select :reparto_id)
              (hh/from :reparto)
              (hh/where [:= :reparto_codice reparto-codice] [:= :reparto_presidio_id presidio-id]))]
    (db/query! datasource q)))
