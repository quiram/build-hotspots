## Introduction

Build Hotspots is (or rather will be) a CI/CD visualisation tool that analyses build data and shows the builds that are run most often and how long they take in a graphical way; some people have described this as a "4D representation of the build system."

<blockquote class="twitter-tweet" data-partner="tweetdeck"><p lang="en" dir="ltr">I like <a href="https://twitter.com/AbrahamMarin">@AbrahamMarin</a> Build-Driven Architecture, using auto-generated 4D dependency graphs. Thanks to <a href="https://twitter.com/skillsmatter">@skillsmatter</a> <a href="http://t.co/0i4AvgfrWe">pic.twitter.com/0i4AvgfrWe</a></p>&mdash; michael aubert (@michaelaubertfr) <a href="https://twitter.com/michaelaubertfr/status/601468704803557376">May 21, 2015</a></blockquote>
<script async src="//platform.twitter.com/widgets.js" charset="utf-8"></script>

To be fair, it's not automated yet, that's what this project is about. To know more about what Build Hotspots can be used for, watch this <a href="https://www.parleys.com/play/what-your-build-telling-you-about-your-application-structure">short presentation</a> (it's about 10 minutes only) that I gave at Devoxx UK 2015. If you have more time and want to know more about its motivation and potential, check <a href="http://www.oreilly.com/pub/e/3514">this webcast</a> I gave for O'Reilly (requires free registration).

## How to run BuildHotspots

There are currently two ways to run BuildHotspots:
- Jenkins Connector: the application will prompt you to indicate the URL of your Jenkins instance. Run the class `JenkinsBasedBuildHotspotsApplication`.
- File Connector: used mostly for testing since you'll to provide an XML file (there are samples in the `resources` folder); you're free to use it nonetheless. Run the class `FileBasedBuildHotspotsApplication`.

### "I need more detailed instructions"

Sure thing, here you go:

Download a copy of BuildHotspots
```bash
git clone https://github.com/quiram/build-hotspots.git
```
Build it
```
cd build-hotspots
mvn clean verify
```
Run it
```
cd target
java -cp buildhotspots-1.0-SNAPSHOT.jar com.github.quiram.buildhotspots.visualisation.JenkinsBasedBuildHotspotsApplication
# Or use the File Connector class if that's your preference
```

### "This application is buggy as f***"

Yeah, you're probably right. There are three things to take into account though:

1. I haven't used JavaFX before. Nor Swing. Nor I have any experience programming graphical interfaces.
2. I have very little time to work on this.
3. This application is still _very_ young, at the moment I'm favouring new features over stability.

So, if you find it frustrating, please be patient. Or even better, please help :)

## Things to do / Milestones

This section is a bit of a brain dump, my initials ideas for Build Hotspots. Now I use it as a sort of task tracker (I know issues are probably better than this, but I started with a text list and I haven't taken the time to migrate that to issues yet). 

- CI clients should be able to:
    - Obtain list of all build configurations -- DONE
    - Obtain upstream / downstream dependencies of a build configuration -- DONE
    - Obtain date of a particular build run
    - Obtain oldest available build run
    - Obtain size duration of a particular build run
    - Obtain total number of build runs since records began for a particular build configuration

- CI clients to support
    - Jenkins Client  -- IN PROGRESS
    - TeamCity Client

- Data processing
    - Collect all builds
    - Visitor pattern?
        - Apply colour: get most frequent build, assign frequency to MAX, then least frequent build, assign frequency as MIN,
 then assign colour to all builds according to relative frequency. Assuming a gradient blue -> red, then the colour for
 a build wit frequency F will be: colour = blue * (MAX - F) / (MAX - MIN) + red * (F - MIN) / (MAX - MIN)

- Visualisation (JavaFX?)
    - Graph DAG with colours and sizes -- IN PROGRESS
    - Hover to display build configuration name -- DONE (through right-click)

- Container (would have to be done in JS, not sure about integration)
    - Chrome plugin
    - Mozilla plugin

- Options (to be applied through config file, through plugin options, etc.)
    - Filter build configurations to be shown in the graph. -- DONE
        - Instead of selecting all of them, select only a few and then add an option to include upstream / downstream from those. -- DONE
    - Option to use build run frequency directly (assuming all build data is available) or through oldest available builds
(assuming it's not)
    - Auto-detect CI client, simply indicate the URL and guess what the client is from the response.
