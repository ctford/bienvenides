(ns bienvenides.encoding
  (:require [leipzig.melody :as melody]
            [leipzig.scale :as scale]
            [clojure.string :as str]
            [cljs.pprint :as pprint]))

(defn encode-pitch [persons-name]
  (letfn [(index [c] (- (pprint/char-code c) (pprint/char-code \a)))]
    (->> persons-name
      (map index)
      (map #(mod % 5)))))

(defn is-vowel? [letter]
  (#{\a \e \i \o \u} letter))

(defn encode-one-duration [letter]
  (if (is-vowel? letter)
    1
    0.5))

(defn encode-duration [persons-name]
  (map encode-one-duration persons-name))

(defn encode-one [persons-name]
  (let [pitches (encode-pitch persons-name)
        durations (encode-duration persons-name)]
    (melody/phrase durations pitches)))

(defn encode [names]
  (->> names
       (map str/lower-case)
       (map encode-one)
       (map #(->> %2 (melody/where :pitch (scale/from (* 5 %1)))) (range))
       (reduce melody/with)))
