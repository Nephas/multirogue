(ns rlclient.render.loading
  (:require [rlclient.graphics.sheets :refer [image]]
            [quil.core :as q]
            [rlclient.graphics.camera :as c]))

(def loading-text {0 "The World has ended - for how long you do not know. Life is a burden and Death a treasure.
But True Death does not come easily in this place beyond time,
where the living eat the dead, and the dead eat the living.

But the lonely Godi has told you so often - there must be a place, where you can leave it behind.
A place where the Raven will not find you, and drag your rotting body back into the eternal circle.
A place - to find peace.

Press [SPACE] to Continue"

                   1 "Press [R] to Restart"
                   })

(defn render-loadscreen! [screen]
  (let [[resx resy] c/MAPRES
        h (int (* 0.5 resx))]
    (q/background 0)
    (q/image (get @image screen) 0 0 resx h)
    (q/fill 1.0)
    (q/text-align :left)
    (q/text (get loading-text screen) 0 (+ 50 h))
    ))