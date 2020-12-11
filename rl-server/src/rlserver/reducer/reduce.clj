(ns rlserver.reducer.reduce
  (:require [rlserver.system.action :refer [active?]]
            [rlserver.reducer.move :refer [entity-move moveable?]]
            [rlserver.reducer.attack :refer [entity-attack]]))

(defn reduce [state action pid]
  (let [[primary secondary] action]
    (cond (= :pass primary)
          state

          (= :move primary)
          (if (and (moveable? state secondary pid) (active? state pid))
            (entity-move state pid secondary)
            state)

          (= :attack primary)
          (if (active? state pid)
            (entity-attack state pid secondary)
            state)

          true state)))