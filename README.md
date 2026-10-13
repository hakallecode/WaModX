<div align="center">

<picture>
  <source media="(prefers-color-scheme: dark)" srcset="./assets/brand/title_dark.png">
  <source media="(prefers-color-scheme: light)" srcset="./assets/brand/title_light.png">
  <img src="./assets/brand/title_light.png" width="300" alt="WaModX">
</picture>

<br>

<picture>
  <source media="(prefers-color-scheme: dark)" srcset="./assets/brand/stars_dark.png">
  <source media="(prefers-color-scheme: light)" srcset="./assets/brand/stars_light.png">
  <img src="./assets/brand/stars_light.png" height="24" alt="An unofficial WhatsApp mod built against stubs.">
</picture>

<br>
<br>

<p>
  <img src="https://img.shields.io/badge/2.26.36.70-25D366?style=flat-square&logo=whatsapp&logoColor=white">
  &nbsp;
  <img src="https://img.shields.io/badge/Java-11-1a1a1a?style=flat-square&logo=openjdk&logoColor=white">
  &nbsp;
  <img src="https://img.shields.io/badge/license-MIT-25D366?style=flat-square">
  &nbsp;
  <img src="https://img.shields.io/badge/Unofficial-1a1a1a?style=flat-square">
</p>

</div>

<p align="center">
  <b>WaModX</b> is an <b>unofficial WhatsApp mod</b> shipped as a <b>standalone APK</b>
  built against hand-written stubs — never against WhatsApp's real classes.
  <br>
  A separate smali patchset in
  <a href="https://github.com/SoughtXp/whatsapp-apktool"><code>whatsapp-apktool</code></a>
  rewires the host APK to call into these hooks.
</p>

<p align="center"><b>End-to-end loop</b></p>

<p align="center">
  <img src="https://img.shields.io/badge/①_WhatsApp_APK-1a1a1a?style=flat-square&logo=whatsapp&logoColor=white">
  →
  <img src="https://img.shields.io/badge/②_apktool_decode-1a1a1a?style=flat-square&logo=android&logoColor=white">
  →
  <img src="https://img.shields.io/badge/③_map_names-1a1a1a?style=flat-square">
  →
  <img src="https://img.shields.io/badge/④_write_stubs-25D366?style=flat-square&logo=openjdk&logoColor=white">
  →
  <img src="https://img.shields.io/badge/⑤_build_WaModX-25D366?style=flat-square">
  <br>
  <img src="https://img.shields.io/badge/⑥_smali_hooks-25D366?style=flat-square">
  →
  <img src="https://img.shields.io/badge/⑦_rebuild_APK-1a1a1a?style=flat-square&logo=android&logoColor=white">
  →
  <img src="https://img.shields.io/badge/⑧_sign-1a1a1a?style=flat-square">
  →
  <img src="https://img.shields.io/badge/⑨_install-25D366?style=flat-square&logo=android&logoColor=white">
  →
  <img src="https://img.shields.io/badge/⑩_iterate-25D366?style=flat-square">
</p>

<picture>
  <source media="(prefers-color-scheme: dark)" srcset="./assets/headings/features_dark.png">
  <source media="(prefers-color-scheme: light)" srcset="./assets/headings/features_light.png">
  <img src="./assets/headings/features_light.png" height="34" alt="Features">
</picture>

<br>

<p>
<img src="./assets/icons/check-done.svg" width="18" alt="done"> <b>Anti Revoke Status</b><br>
<img src="./assets/icons/check-done.svg" width="18" alt="done"> <b>Anti Revoke Messages</b><br>
<img src="./assets/icons/check-done.svg" width="18" alt="done"> <b>Freeze Last Seen</b><br>
<img src="./assets/icons/check-done.svg" width="18" alt="done"> <b>Hide Status View</b><br>
<img src="./assets/icons/check-done.svg" width="18" alt="done"> <b>Hide Blue Ticks</b><br>
<img src="./assets/icons/check-done.svg" width="18" alt="done"> <b>Blue Ticks After Reply</b><br>
<img src="./assets/icons/check-done.svg" width="18" alt="done"> <b>Hide Typing / Recording Audio</b><br>
<img src="./assets/icons/check-pending.svg" width="18" alt="pending"> <b>Anti View Once</b><br>
<img src="./assets/icons/check-done.svg" width="18" alt="done"> <b>Download Status / Copy Status Text</b><br>
<img src="./assets/icons/check-pending.svg" width="18" alt="pending"> <b>Bypass Anti Ban</b><br>
<img src="./assets/icons/check-pending.svg" width="18" alt="pending"> <b>Fix SMS</b><br>
<img src="./assets/icons/check-pending.svg" width="18" alt="pending"> <b>Design Mods</b>
</p>

<br>

<picture>
  <source media="(prefers-color-scheme: dark)" srcset="./assets/headings/map_dark.png">
  <source media="(prefers-color-scheme: light)" srcset="./assets/headings/map_light.png">
  <img src="./assets/headings/map_light.png" height="34" alt="Map">
</picture>

<br>

Every WhatsApp release reshuffles the obfuscated class names like `LX/1Ex`,
which is why the two repos always move together: refresh the decompile
first, then re-check the stubs against it.

<p>
<img src="https://img.shields.io/badge/①_WhatsApp_APK-1a1a1a?style=flat-square&logo=whatsapp&logoColor=white"> the target release<br>
<a href="https://github.com/SoughtXp/whatsapp-apktool"><img src="https://img.shields.io/badge/②_Decompile-25D366?style=flat-square&logo=android&logoColor=white"></a> apktool output, where the real names live<br>
<a href="./stubs/src"><img src="https://img.shields.io/badge/③_Stubs-25D366?style=flat-square&logo=openjdk&logoColor=white"></a> one per class, <code>compileOnly</code>, never shipped<br>
<a href="./app"><img src="https://img.shields.io/badge/④_WaModX-25D366?style=flat-square"></a> built against the stubs above
</p>

<br>

<picture>
  <source media="(prefers-color-scheme: dark)" srcset="./assets/headings/contributing_dark.png">
  <source media="(prefers-color-scheme: light)" srcset="./assets/headings/contributing_light.png">
  <img src="./assets/headings/contributing_light.png" height="34" alt="Contributing">
</picture>

<br>

The most useful PR you can send is a stub that stopped matching after a
WhatsApp update. One concern per PR keeps it reviewable, and easy to
revert on its own if it turns out to be wrong.

<p>
<img src="https://img.shields.io/badge/①_Fork-25D366?style=flat-square&logo=github&logoColor=white"> your own copy
<a href="https://github.com/SoughtXp/WaModX/fork">
  <img src="https://img.shields.io/badge/Fork_this_repo-24292F?style=flat-square&logo=github&logoColor=white" height="22" alt="Fork WaModX">
</a><br>
<img src="https://img.shields.io/badge/②_Branch-1a1a1a?style=flat-square"> one per change<br>
<img src="https://img.shields.io/badge/③_Commit-1a1a1a?style=flat-square"> one concern per commit<br>
<img src="https://img.shields.io/badge/④_Push-1a1a1a?style=flat-square"> to your fork<br>
<a href="../../pulls"><img src="https://img.shields.io/badge/⑤_Pull_Request-25D366?style=flat-square&logo=github&logoColor=white"></a> against <code>main</code>
</p>

<br>

<picture>
  <source media="(prefers-color-scheme: dark)" srcset="./assets/headings/license_dark.png">
  <source media="(prefers-color-scheme: light)" srcset="./assets/headings/license_light.png">
  <img src="./assets/headings/license_light.png" height="34" alt="License">
</picture>

<p>Released under the <a href="./LICENSE"><b>MIT License</b></a>.</p>
