# BYOND Dmi Util

## About 

Small set of util classes to work with BYOND dmi files.

## How To Use

### DmiSlurper

Class used to deserialize `.dmi` file.
 - `slurpUp(final File dmiFile)` - from file.
 - `slurpUp(final String dmiName, final String base64content)` - from base64.
 - `slurpUp(final String dmiName, final InputStream input)` - from any input stream.

As a result of deserialization `Dmi` object returns.

### DmiComparator

Class used to compare two dmi's and show difference between them.
 - `compare(@Nullable final Dmi oldDmi, @Nullable final Dmi newDmi)`
 
 As a result of comparison `DmiDiff` object returns.
 
 
More could be found in JavaDoc.