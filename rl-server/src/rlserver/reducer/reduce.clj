(ns rlserver.reducer.reduce
  (:require [rlserver.reducer.move :refer [entity-move moveable?]]
            [rlserver.reducer.attack :refer [entity-attack]]))

(defn reduce [state action pid]
  (let [[primary secondary] action]
    (cond (= :pass primary)
          state

          (= :move primary)
          (if (moveable? state secondary pid)
            (entity-move state pid secondary)
            (entity-attack state pid secondary))

          true state)))