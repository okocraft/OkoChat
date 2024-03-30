# Plans for Improving LunaChat

LunaChat has a very long history from 2013, and it was originally a Bukkit plugin.

## Native running on Velocity

In 2020, LunaChat started supporting for BungeeCord. But BungeeCord is a legacy and its fork "Waterfall" has reached EoL.

The current major proxy choice is Velocity. In OKOCRAFT, running LunaChat using [snap](https://github.com/Phoenix616/Snap) on Velocity.

Also, in current development, using [Adventure](https://github.com/KyoriPowered/adventure) for constructing (styled) messages is a recommended way.

We need to go away from legacy formatting, raw json messages, and string message handling.

In summary, it is time to modernize LunaChat.

### Plans

Rewriting LunaChat is painful and heavy work, so we need to take things one at a time.

Detailed plans are on [the GitHub project](https://github.com/orgs/okocraft/projects/9/)

#### Component-based message handling

Using legacy-formatted messages for sending is deprecated in Paper (and Velocity does not support it),
so we need to migrate raw-string handling to component-based handling.

#### Separate bridge features from the plugin itself

Currently, sending chat messages from Spigot to a proxy is built into LunaChat itself. 
This should be eliminated and more APIs can be provided from the bridge.

For example, [LunaChatInfo](https://github.com/okocraft/LunaChatInfo), a plugin that gets the default channel of the player from the backend server,
can be merged into the bridge feature. Additionally, we can provide PlaceholderAPI support.

#### Thread-Safety

In Velocity (also in BungeeCord), events and command executions are run async. 
To prevent data corruption, we need to make LunaChat objects thread-safe.
