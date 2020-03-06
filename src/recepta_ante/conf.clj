(ns recepta-ante.conf
  (:require
    [cprop.core :refer [load-config]]
    [cprop.source :as source]))
    
(def configuration (load-config))
