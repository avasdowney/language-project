(ns reddit-scraper.core
  (:require [net.cgrand.enlive-html :as html]
            [clj-http.client :as client]))

"We need a User-Agent to pretend we're a browser"
(def h {"User-Agent" "Mozilla/5.0 (Windows NT 6.1;) Gecko/20100101 Firefox/13.0.1"})

"sets url to pull from"
(def ^:dynamic *dad-jokes-url* "https://www.reddit.com/r/dadjokes/hot.json")

"pulls from java.net.url class to retrieve url... not needed as fetch-json used instead"
(defn fetch-url [url]
  (html/html-resource (java.net.URL. url)))

"pull structured json file directly from reddit"
(defn fetch-json [url]
  (:body (client/get url {:headers h :as :json-strict})))

"fetches all of the dad jokes, returns a list of the raw jokes"
(defn dad-jokes-raw []
  (:children (:data (fetch-json *dad-jokes-url*)))
  )

"pulls all data from child array into a list"
(defn dad-jokes-data []
  (map #(:data %) (dad-jokes-raw))
  )

"given the raw data, pull out just what we want"
(defn select-joke [data]
  (select-keys data [:title :selftext :author])
  )

"show all of the jokes with just what want"
(defn dad-jokes []
  (map #(select-joke %) (dad-jokes-data))
  )
