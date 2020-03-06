(ns recepta-ante.utils
  (:require
   [cambium.core :as log]))




(defn string->date [s]
  (if (and s (re-matches #"^([0-2][0-9]||3[0-1])/(0[0-9]||1[0-2])/([0-9][0-9])?[0-9][0-9]$" s))
    (.parse
      (java.text.SimpleDateFormat. "dd/MM/yyyy") s)
    nil))

(defn date->string [s]
  (.format
    (java.text.SimpleDateFormat. "dd/MM/yyyy") s))

(defn str->integer
  [s]
  (if (every? #(Character/isDigit %) s)
    (Integer/parseInt s)
    0))


(defn strings->integers
  [xs]
  (if (sequential? xs)
    (map str->integer xs)
    (str->integer xs)))

(defmacro with-err-str
    "Evaluates exprs in a context in which *err* is bound to a fresh
    StringWriter.  Returns the string created by any nested printing
    calls."
    [& body]
    `(let [s# (new java.io.StringWriter)]
         (binding [*err* s#]
           ~@body
           (str s#))))

(defn despace [m]
  (zipmap (map #(keyword (clojure.string/replace (name %) " " "_")) (keys m))
      (vals m)))


(defn stacktrace
  [exception]
  (log/error  exception "Email notification failed"))
