## Introduction

Build Hotspots is (or rather will be) a CI/CD visualitation tool that analyses build data and shows the builds that are run most often and how long they take in a graphical way; some people have described this as a "4D representation of the build system."

<blockquote class="twitter-tweet" data-partner="tweetdeck"><p lang="en" dir="ltr">I like <a href="https://twitter.com/AbrahamMarin">@AbrahamMarin</a> Build-Driven Architecture, using auto-generated 4D dependency graphs. Thanks to <a href="https://twitter.com/skillsmatter">@skillsmatter</a> <a href="http://t.co/0i4AvgfrWe">pic.twitter.com/0i4AvgfrWe</a></p>&mdash; michael aubert (@michaelaubertfr) <a href="https://twitter.com/michaelaubertfr/status/601468704803557376">May 21, 2015</a></blockquote>
<script async src="//platform.twitter.com/widgets.js" charset="utf-8"></script>

To be fair, it's not automated yet, that's what this project is about. To know more about what Build Hotspots can be used for, whatch this <a href="https://www.parleys.com/play/what-your-build-telling-you-about-your-application-structure">short presentation</a> (it's about 10 minutes only) that I gave at Devoxx UK 2015.

## Things to do / Milestones

- CI clients should be able to:
    - Obtain list of all build configurations -- DONE
    - Obtain upstream / downstream dependencies of a build configuration
    - Obtain date of a particular build run
    - Obtain oldest available build run
    - Obtain size duration of a particular build run
    - Obtain total number of build runs since records began for a particular build configuration

- CI clients to support
    - Jenkins Client
    - TeamCity Client


- Data processing
    - Collect all builds
    - Visitor pattern?
        - Apply colour: get most frequent build, assign frequency to MAX, then least frequent build, assign frequency as MIN,
 then assign colour to all builds according to relative frequency. Assuming a gradient blue -> red, then the colour for
 a build wit frequency F will be: colour = blue * (MAX - F) / (MAX - MIN) + red * (F - MIN) / (MAX - MIN)


- Visualisation (JavaFX?)
    - Graph DAG with colours and sizes
    - Hover to display build configuration name


- Container (would have to be done in JS, not sure about integration)
    - Chrome plugin
    - Mozilla plugin


- Options (to be applied through config file, through plugin options, etc.)
    - Filter build configurations to be shown in the graph.
        - Instead of selecting all of them, select only a few and then add an option to include upstream / downstream from those.
    - Option to use build run frequencty directly (assuming all build data is available) or through oldest available builds
(assuming it's not)
    - Auto-detect CI client, simply indicate the URL and guess what the client is from the response.
