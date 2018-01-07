/*
 *   Copyright 2018 Jasha Joachimsthal
 *
 *   Licensed under the Apache License, Version 2.0 (the "License");
 *   you may not use this file except in compliance with the License.
 *   You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 *   Unless required by applicable law or agreed to in writing, software
 *   distributed under the License is distributed on an "AS IS" BASIS,
 *   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *   See the License for the specific language governing permissions and
 *   limitations under the License.
 */

(function ($, window) {
  var UI_MODES = {
    ENRICHED: 'enriched',
    PLAIN: 'plain'
  };
  var uiMode = UI_MODES.ENRICHED;
  var uiModeStorageKey = 'uiMode';

  function initInlineEditable() {
    $('.inline-editable').editable({mode: 'inline'});
  }

  function uiStandardHtml() {
    $('.inline-editable').editable('destroy');
    $('.city-link')
        .removeAttr('role')
        .removeAttr('data-toggle')
        .removeAttr('data-target')
        .removeAttr('data-remote')
        .removeClass('btn btn-default');
  }

  function uiEnriched() {
    initInlineEditable();

    // change all city-link links into modal triggers, but don't let Bootstrap do its magic loading the content into the
    // modal-content div because it messes up the UI.
    $('.city-link')
        .attr('role', 'button')
        .attr('data-toggle', 'modal')
        .attr('data-target', '#cityModal')
        .attr('data-remote', 'false')
        .addClass('btn btn-default');
  }

  function toggleUi() {
    var uiMode = getUiMode();
    if (uiMode === UI_MODES.ENRICHED) {
      uiStandardHtml();
      setUiMode(UI_MODES.PLAIN)
    }
    else {
      uiEnriched();
      setUiMode(UI_MODES.ENRICHED);
    }
  }

  function addButtonToToggleUi() {
    var $toggleUiBtn = $('<a></a>')
        .attr('role', 'button')
        .text('Toggle UI')
        .on('click', toggleUi);
    $('#ui-toggle').html($toggleUiBtn).removeClass('hidden');
  }

  function supportsSessionStorage() {
    if (window.sessionStorage === undefined) {
      return false;
    }

    try {
      window.sessionStorage.setItem('test', '');
      window.sessionStorage.removeItem('test');
      return true;
    }
    catch (e) {
      return false;
    }
  }

  function getUiMode() {
    if (supportsSessionStorage()) {
      var storedMode = window.sessionStorage.getItem(uiModeStorageKey) || '';
      if (storedMode.length > 0) {
        uiMode = storedMode;
      }
    }
    return uiMode;
  }

  function setUiMode(_uiMode) {
    if (supportsSessionStorage()) {
      window.sessionStorage.setItem(uiModeStorageKey, _uiMode);
    }
    else {
      uiMode = _uiMode;
    }
  }

  function initUi() {
    var preferredUiMode = getUiMode();
    if (preferredUiMode === UI_MODES.ENRICHED) {
      uiEnriched();
    }
    addButtonToToggleUi();
  }

  initUi();
})(jQuery, window);