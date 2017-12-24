/**
 * @providesModule RNGZip
 * @flow
 */
'use strict'

var NativeRNGZip = require('react-native').NativeModules.RNGZipManager

exports.gunzip = function gunzip (inputFile, outputFile) {
  return NativeRNGZip.gunzip(inputFile, outputFile)
}

exports.getUnpackedContextFromGzipFile = function getUnpackedContextFromGzipFile (file) {
  return NativeRNGZip.getUnpackedContextFromGzipFile(file)
}
