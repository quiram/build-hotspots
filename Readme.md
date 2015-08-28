## Things to do / Milestones

- CI clients should be able to:
    - Obtain list of all build configurations
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


- Visualisation (JS?)
    - Graph DAG with colours and sizes
    - Hover to display build configuration name


- Container
    - Chrome plugin
    - Mozilla plugin


- Options (to be applied through config file, through plugin options, etc.)
    - Filter build configurations to be shown in the graph.
        - Instead of selecting all of them, select only a few and then add an option to include upstream / downstream from those.
    - Option to use build run frequencty directly (assuming all build data is available) or through oldest available builds
(assuming it's not)
    - Auto-detect CI client, simply indicate the URL and guess what the client is from the response.
