(ns recepta-ante.ext_packt.kata)


(defn check [number counter total]
  (if (int? (/ number (inc counter)))
    (inc total)
    total))

(defn get-tot-integer-coord
  [x]
  (let [number (Math/round x)]
    (loop [counter 0
           total 0]
      (if (> counter (/ number 2))
        total
        (recur (inc counter) (check number counter total))))))

(defn c [k]
  (let [k32 (* k (Math/sqrt k))
        is-int (= (mod k32 1) 0.0)]
    (println (str k32 " " is-int))
    (if is-int
      (get-tot-integer-coord k32)
      0)))
