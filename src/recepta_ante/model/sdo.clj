(ns recepta-ante.model.sdo
  (:require
   [honeysql.helpers :as hh]
   [recepta-ante.services.database :as db]
   [recepta-ante.model.reparto :as reparto-dao]))

(defn select-by-codice-presidio
  [datasource sdo-codice presidio-id]
  (let [q (-> (hh/select :*)
              (hh/from :sdo)
              (hh/where [:= :sdo_codice sdo-codice] [:= :sdo_presidio_id presidio-id]))]
    (db/query! datasource q)))

(defn exists [datasource map-sdo]
  (let [sdo-codice (str (get-in map-sdo [:sdo :codice]))
        presidio-id (get-in map-sdo [:sdo :presidio :id])
        sdo-extracted (select-by-codice-presidio datasource sdo-codice presidio-id)]
    (if (= (count sdo-extracted) 0) true false)))

(defn insert!
  [datasource map-sdo]
  (let [{:keys [persona sdo]} map-sdo
        {codice :codice presidio :presidio reparto :reparto regime :regime data-ricovero :data-ricovero data-dimissione :data-dimissione } sdo
        {codice-fiscale :codice-fiscale cognome :cognome nome :nome genere :sesso data-nascita :data-nascita} persona
        {presidio-id :id presidio-codice :codice presidio-descrizione :descrizione} presidio
        {reparto-codice :codice reparto-descrizione :descrizione} reparto
        reparto-id (:reparto-id (first (reparto-dao/select-id datasource reparto-codice presidio-id)))
        q (-> (hh/insert-into :sdo)
              (hh/columns :sdo_codice
                          :sdo_codicefiscale
                          :sdo_cognome
                          :sdo_nome
                          :sdo_nominativo
                          :sdo_genere
                          :sdo_datanascita
                          :sdo_dataricovero
                          :sdo_regimericovero
                          :sdo_presidio_id
                          :sdo_presidio_codice
                          :sdo_presidio_desc
                          :sdo_reparto_id
                          :sdo_reparto_codice
                          :sdo_reparto_desc)
              (hh/values [[codice
                           codice-fiscale
                           cognome
                           nome
                           (str cognome " " nome)
                           genere
                           data-nascita
                           data-ricovero
                           regime
                           presidio-id
                           presidio-codice
                           presidio-descrizione
                           reparto-id
                           reparto-codice
                           reparto-descrizione]]))]
      (db/execute! datasource q)))
