(ns recepta-ante.ext_packt.3_functions)

;; ############### SEQUENTIAL DESTRUCTURING (VECTOR)

(def bob-booking [1425,
                  "Bob Smith",
                  "Allergic to unsalted peanuts only",
                  [[48.9615, 2.4372], [37.742, -25.6976]],
                  [[37.742, -25.6976], [48.9615, 2.4372]]])

(def my-flight [[48.9615, 2.4372], [37.742 -25.6976]])

(defn print-booking [booking]
  (let [[id customer-name sensitive-info flight1 flight2 flight3] booking]
    (println id customer-name flight1 flight2 flight3)))

;; how to ignore some parts of the data with destructuring.
(defn print-partial-booking [booking]
  (let [[_ customer-name _ flight1 flight2 flight3] booking]
    (println customer-name flight1 flight2 flight3)))

;;Bind the flights sequence to a flights symbol by using the & character
(defn print-partial-binded-booking [booking]
  (let [[_ customer-name _ & flights] booking]
     (println (str customer-name " booked " (count flights) " flights."))))

;;nested destructuring (nested vectors)
(defn print-flight [flight]
  (let [[[lat1 lon1] [lat2 lon2]] flight]
    (println (str "Flying from: Lat " lat1 " Lon " lon1 " Flying to: Lat " lat2 " Lon " lon2))))

;;decomposing nested structure in multiple let bindings
(defn print-flight-2 [flight]
  (let [[departure arrival] flight
        [lat1 lon1] departure
        [lat2 lon2] arrival]
    (println (str "Flying from: Lat " lat1 " Lon " lon1 " Flying to: Lat " lat2 " Lon " lon2))))

;;all toghether
(defn print-booking-all [booking]
  (let [[_ customer-name _ & flights] booking]
    (println (str customer-name " booked " (count flights) " flights."))
    (let [[flight1 flight2 flight3] flights]
      (when flight1 (print-flight-2 flight1))
      (when flight2 (print-flight-2 flight2))
      (when flight3 (print-flight-2 flight3)))))

;; ########################### ASSOCIATIVE DESTRUCTURING (MAPS)
(def mapjet-booking
     {:id 8773
      :customer-name "Alice Smith"
      :catering-notes "Vegetarian on Sundays"
      :flights [
                {:from {:lat 48.9615 :lon 2.4372 :name "Paris Le Bourget Airport"}
                 :to {:lat 37.742 :lon -25.6976 :name "Ponta Delgada Airport"}}
                {:from {:lat 37.742 :lon -25.6976 :name "Ponta Delgada Airport"}
                 :to {:lat 48.9615 :lon 2.4372 :name "Paris Le Bourget Airport"}}]})


(defn destruct-1 [m]
  (let [{:keys [customer-name flights]} m]
    (println (str customer-name " booked " (count flights) " flights."))))


(defn print-mapjet-flight [flight]
  (let [{:keys [from to]} flight
        {lat1 :lat lon1 :lon} from
        {lat2 :lat lon2 :lon} to]
    (println (str "Flying from: Lat " lat1 " Lon " lon1 " Flying to: Lat " lat2 " Lon " lon2))))


(defn print-mapjet-flight-2 [flight]
  (let [{{lat1 :lat lon1 :lon} :from,
         {lat2 :lat lon2 :lon} :to}
        flight]
    (println (str "Flying from: Lat " lat1 " Lon " lon1 " Flying to: Lat " lat2 " Lon " lon2))))


(defn print-mapjet-booking [booking]
  (let [{:keys [customer-name flights]} booking]
    (println (str customer-name " booked " (count flights) " flights."))
    (let [[flight1 flight2 flight3] flights]
      (when flight1 (print-mapjet-flight flight1)) flights
      (when flight2 (print-mapjet-flight flight2))
      (when flight3 (print-mapjet-flight flight3)))))
