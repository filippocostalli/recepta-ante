(ns recepta-ante.services.csv-service
  (:require
    [ultra-csv.core]
    [recepta-ante.utils :as myutils]
    [recepta-ante.conf :as conf]
    [recepta-ante.model.reparto :as reparto-dao]))

(def presidio-sirai {:id 1 :codice "200098" :descrizione "P.O. SIRAI"})
(def presidio-nsbn {:id 5 :codice "200034" :descrizione "P.O. N.S. DI BONARIA"})
(def presidio-sbarbara {:id 2 :codice "200029" :descrizione "P.O. SANTA BARBARA"})
(def john-doe-cf "XXXXXXXXXXXXXXXX")

(def input-data-csv
  (ultra-csv.core/read-csv
    (:input-file-csv conf/configuration) {:header? false :skip 1}))

(defn clean-codice-reparto [codice]
  (case codice
    9.01 "09.01"
    8.01 "09.01"
    801 "0801"
    901 "0901"
    codice))

(defn get-reparto-sirai
  [codice descrizione]
  {:codice (str (clean-codice-reparto codice))
   :descrizione (clojure.string/trim
                  (clojure.string/upper-case (subs descrizione 0 (clojure.string/index-of descrizione "*"))))})

(defn get-reparto
  [codice descrizione]
  {:codice (str (clean-codice-reparto codice))
   :descrizione (clojure.string/trim (clojure.string/upper-case descrizione))})

(defn get-codice-fiscale [s]
  (if (or (clojure.string/blank? s) (= (count s) 0))
    john-doe-cf
    (clojure.string/upper-case s)))

(defn get-genere [s]
  (if (or (clojure.string/blank? s) (= (count s) 0))
    "M"
    (clojure.string/upper-case s)))

(defn parse-data-sirai [v]
    ;;(println v)
    {:persona
        {:codice-fiscale (get-codice-fiscale  (nth v 11))
         :cognome (clojure.string/upper-case (nth v 2))
         :nome (clojure.string/upper-case (nth v 3))
         :sesso (get-genere (nth v 4))
         :data-nascita (myutils/string->date (nth v 5))}
      :sdo
        {:presidio presidio-sirai
         :reparto (get-reparto-sirai (nth v 7) (nth v 8))
         :codice (nth v 0)
         :regime (clojure.string/upper-case (nth v 6))
         :data-ricovero (myutils/string->date (nth v 9))
         :data-dimissione (myutils/string->date (nth v 10))}})

(defn parse-data-nsbn [v]
    ;;(println v)
    {:persona
        {:codice-fiscale (get-codice-fiscale  (nth v 10))
         :cognome (clojure.string/upper-case (nth v 2))
         :nome (clojure.string/upper-case (nth v 3))
         :sesso (clojure.string/upper-case (nth v 6))
         :data-nascita (myutils/string->date (nth v 4))}
      :sdo
        {:presidio presidio-nsbn
         :reparto (get-reparto (nth v 15) (nth v 16))
         :codice (nth v 1)
         :regime (clojure.string/upper-case (nth v 6))
         :data-ricovero (myutils/string->date (nth v 13))
         :data-dimissione nil}})

(defn parse-data-sbarbara [v]
  (update-in
    (parse-data-sirai v)
    [:sdo]
    assoc :presidio presidio-sbarbara))

(defn get-ente [id]
    (case id
      1 :sirai
      2 :sbarbara
      5 :nsbn))
(defmulti parse-data get-ente)
(defmethod parse-data :sirai [id] (map parse-data-sirai input-data-csv))
(defmethod parse-data :sbarbara [id] (map parse-data-sbarbara input-data-csv))
(defmethod parse-data :nsbn [id] (map parse-data-nsbn input-data-csv))
