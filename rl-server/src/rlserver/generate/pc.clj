(ns rlserver.generate.pc
  (:require [rlserver.entity.entity :refer [create-entity]]))

(defn other [id]
  (get {0 1
        1 0} id))

(defn generate-pc [state id pos]
  (create-entity state
                 {:pos      pos
                  :hp       [5 5]
                  :ap       [0 5]
                  :dmg      3
                  :animated id
                  :follow (other id)}
                 [:blocking :pc :ai]
                 id))