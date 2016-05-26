[Calibre Library to Torrents]() &mdash; Convert your Calibre Library Books to Torrents
==========
![](http://img.shields.io/version/0.0.1.png?color=green)


[Change log](https://github.com/tchoulihan/calibre-library-to-torrents/releases)


Calibre Library to Torrents takes your Calibre library, and creates individual book torrents, containing a folder of:
* All formats of the book you currently have saved(often `epub`, `pdf`)
* The `cover.jpg`
* A `metadata.opf` containing metadata about the book

The torrent file is intelligently named, to:
`author - title [language] [publisher] [ISBN] [CLTT]`


## Installation
### Requirements
- Java 8


## Usage
```sh
wget https://github.com/tchoulihan/calibre-library-to-torrents/releases/download/0.0.1/calibre-library-to-torrents.jar
java -jar calibre-library-to-torrents.jar -calibre_dir ~/Calibre\ Library/ -torrents_dir save_dir/
```
## Building from scratch

To build CalibreToTorrents, run the following commands:
```sh
git clone https://github.com/tchoulihan/calibre-library-to-torrents
cd calibre-library-to-torrents
chmod +x install.sh

# This script does a maven install, and java -jar command
./install.sh -calibre_dir ~/Calibre\ Library/ -torrents_dir save_dir/
```

## Bugs and feature requests
Have a bug or a feature request? If your issue isn't [already listed](https://github.com/tchoulihan/calibre-library-to-torrents/issues/), then open a [new issue here](https://github.com/tchoulihan/calibre-library-to-torrents/issues/new).

## Feature requests / TODOS

