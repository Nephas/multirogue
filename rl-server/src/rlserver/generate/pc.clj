(ns rlserver.generate.pc
  (:require [rlserver.entity.entity :refer [create-entity]]))

(defn generate-pc [state id pos]
  (create-entity state
                 {:pos      pos
                  :hp       [5 5]
                  :ap       [0 5]
                  :animated id}
                 [:blocking]
                 id))