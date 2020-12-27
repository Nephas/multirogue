(ns rlserver.generate.pc
  (:require [rlserver.entity.entity :refer [create-entity]]))

(defn other [id]
  (get {0 1
        1 0} id))

(defn generate-pc [state id name pos]
  (create-entity state
                 {:pos      pos
                  :desc     name
                  :hp       [3 3]
                  :dmg      3
                  :animated (+ id 2)
                  :follow   (other id)}
                 [:block :pc :ai]
                 id))