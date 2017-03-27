# Spotmetrics

Spotmetrics is an open-source Image-Analysis software plugin for automatic chromatophore detection and measurement. This software has been published on the [Frontiers In Physiology Journal][1] and more information about its purpose and use can be found there.

## Already Have Fiji Installed ?

Having [Fiji](http://fiji.sc/) already installed is great and will lower the amount of time it will take to get Spotmetrics setup. 

Proceed to __Install Spotmetrics__

## Install Java, Fiji and Spotmetrics

This section is for those users who need to install everything in order to get started.

### Install Java

The very first step that needs to be done is to make sure that the latest version of Java is installed. Follow the steps below to install the latest version of Java.

* Download & Install Java [Here](https://www.java.com/en/download/)
* Make sure Java was installed correctly
  * Visit Oracle's [Verify Java Version Page](https://www.java.com/en/download/installed.jsp)
  * If your browser blocks the Java web-plugin then visit the Oracle's [Manual Java Check Page](https://java.com/en/download/help/version_manual.xml)

Proceed to __Install Fiji__
  
### Install Fiji

Java is installed and now we can proceed with installing [Fiji](http://fiji.sc/).

Go to [Fiji's download](http://fiji.sc/#download) page and download the latest version of Fiji. Make sure to download the "No JRE" version. This will ensure that Fiji will always use the latest verison of Java that is installed on your system.

### Install Spotmetrics Plugin

1. Exit Fiji if it is already running
2. Download the latest version of Spotmetrics Jar file [Here](https://github.com/george-haddad/spotmetrics/releases/latest)
3. Download all dependency Jar files
  * tablelayout.jar
  * poi-3.14-20160307.jar
  * poi-ooxml-3.14-20160307.jar
  * poi-ooxml-schemas-3.14-20160307.jar
  * xmlbeans-2.6.0.jar
4. Place the *Spot_Metrics.jar* file in Fiji's plugins folder `Fiji.app\plugins`
5. If prompted to replace an existing file, click on "Yes"
6. Place all the dependency jar files in Fiji's plugins folder `Fiji.app\plugins`
7. If prompted to replace existing files, you do not need to replace them
8. Start-up Fiji

### Verify Integrity of the downloaded files (optional)

Provided are 3 hash codes to check the integrity of the file *Spot_Metrics.jar*

* md5: 92C90C40EF31D9CECEDBB70CD779F086
* sha1: 4F3CAB39D81E5D6A3A1F2FC30E4CEE631163C078
* sha256: 0F14E4FE0F6B048F09ED2EF069EC750A7CDD8102939E20DDF56CDCC35EB4B0B0

An online check tool can be used like [Online MD5](http://onlinemd5.com/)

## Screenshots

### Configuration Tabs

![spotmetric_viewer_tab](https://cloud.githubusercontent.com/assets/3069650/24379584/55202352-1350-11e7-96e0-0cede29ca069.png)
![spotmetric_proc_tab](https://cloud.githubusercontent.com/assets/3069650/24379577/550065f8-1350-11e7-877e-2fee230d11c8.png)
![spotmetric_tracking_tab](https://cloud.githubusercontent.com/assets/3069650/24379583/551e47ee-1350-11e7-98f3-67fea02b47c5.png)
![spotmetric_analysis_tab](https://cloud.githubusercontent.com/assets/3069650/24379585/5532165c-1350-11e7-8412-8273fa67a45a.png)

### Spot Tracks

![spotmetric_list_tracks_clean](https://cloud.githubusercontent.com/assets/3069650/24379579/550318fc-1350-11e7-81eb-23a507e3a696.png)
![spotmetric_track_menu](https://cloud.githubusercontent.com/assets/3069650/24379578/5501b76e-1350-11e7-8e1c-410cfb06df9f.png)
![spotmetric_track_select_menu_clean](https://cloud.githubusercontent.com/assets/3069650/24379582/551b92b0-1350-11e7-8027-4b742528666e.png)
![spotmetric_edit_track](https://cloud.githubusercontent.com/assets/3069650/24379586/553484fa-1350-11e7-9656-32e9bf388a3b.png)

### Results & Overlays

![spotmetric_excel_data](https://cloud.githubusercontent.com/assets/3069650/24379587/55388eba-1350-11e7-8111-a602f6682f75.png)
![spotmetric_overlay](https://cloud.githubusercontent.com/assets/3069650/24379581/5503ba32-1350-11e7-8eee-272156a08612.png)
![spotmetric_overlay_zoomed](https://cloud.githubusercontent.com/assets/3069650/24379580/55032914-1350-11e7-9abf-1631b096004e.png)

# Authors
* George El-Haddad
  * Software Engineer
* Stavros Hadjisolomou
  * Lead Scientist

# License

Copyright © 2017 Georges El-Haddad

Licensed under the General Public License version 3.0
- [http://www.gnu.org/licenses/gpl-3.0.en.html](http://www.gnu.org/licenses/gpl-3.0.en.html)
- [https://tldrlegal.com/license/gnu-general-public-license-v3-(gpl-3)](https://tldrlegal.com/license/gnu-general-public-license-v3-(gpl-3))
  
[1]: http://journal.frontiersin.org/article/10.3389/fphys.2017.00106/full

