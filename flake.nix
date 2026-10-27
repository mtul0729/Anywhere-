{
  description = "Anywhere- Android development environment";

  inputs.nixpkgs.url = "github:mtul0729/nixpkgs/integration";

  outputs = { self, nixpkgs }:
    let
      system = "x86_64-linux";
      pkgs = import nixpkgs {
        inherit system;
        config = {
          allowUnfree = true;
          android_sdk.accept_license = true;
        };
      };
      android = pkgs.androidenv.composeAndroidPackages {
        platformVersions = [ "35" ];
        buildToolsVersions = [
          "34.0.0"
          "35.0.0"
        ];
        includeNDK = true;
        ndkVersion = "25.0.8775105";
        includeCmake = true;
        cmakeVersions = [ "3.22.1" ];
        includeEmulator = false;
        includeSystemImages = false;
      };
      androidSdk = android.androidsdk;
      androidHome = "${androidSdk}/libexec/android-sdk";
      androidHomeWithNinja = pkgs.runCommandLocal "anywhere-android-sdk" { } ''
        mkdir -p "$out/libexec/android-sdk"
        for entry in "${androidHome}"/*; do
          ln -s "$entry" "$out/libexec/android-sdk/"
        done
        rm "$out/libexec/android-sdk/cmake"
        mkdir -p "$out/libexec/android-sdk/cmake/3.22.1"
        ln -s "${androidHome}/cmake/3.22.1/bin" "$out/libexec/android-sdk/cmake/3.22.1/bin"
        rm "$out/libexec/android-sdk/cmake/3.22.1/bin"
        mkdir "$out/libexec/android-sdk/cmake/3.22.1/bin"
        ln -s "${androidHome}/cmake/3.22.1/bin/cmake" "$out/libexec/android-sdk/cmake/3.22.1/bin/cmake"
        ln -s "${androidHome}/cmake/3.22.1/bin/cpack" "$out/libexec/android-sdk/cmake/3.22.1/bin/cpack"
        ln -s "${androidHome}/cmake/3.22.1/bin/ctest" "$out/libexec/android-sdk/cmake/3.22.1/bin/ctest"
        ln -s "${pkgs.ninja}/bin/ninja" "$out/libexec/android-sdk/cmake/3.22.1/bin/ninja"
      '';
    in
    {
      devShells.${system}.default = pkgs.mkShell {
        packages = [
          pkgs.zulu17
          androidSdk
          android.platform-tools
          pkgs.ninja
          pkgs.pkg-config
          pkgs.unzip
          pkgs.zip
        ];

        JAVA_HOME = pkgs.zulu17.home;
        ANDROID_HOME = "${androidHomeWithNinja}/libexec/android-sdk";
        ANDROID_SDK_ROOT = "${androidHomeWithNinja}/libexec/android-sdk";
        ANDROID_NDK_ROOT = "${androidHomeWithNinja}/libexec/android-sdk/ndk/25.0.8775105";
        GRADLE_OPTS = "-Dorg.gradle.project.android.aapt2FromMavenOverride=${androidHomeWithNinja}/libexec/android-sdk/build-tools/35.0.0/aapt2";

        shellHook = ''
          export PATH="${pkgs.ninja}/bin:${androidHomeWithNinja}/libexec/android-sdk/cmake/3.22.1/bin:$PATH"
          export CMAKE_MAKE_PROGRAM="${pkgs.ninja}/bin/ninja"
        '';
      };
    };
}
