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
        includeEmulator = false;
        includeSystemImages = false;
      };
      androidSdk = android.androidsdk;
    in
    {
      devShells.${system}.default = pkgs.mkShell {
        packages = [
          pkgs.zulu17
          androidSdk
          android.platform-tools
          pkgs.unzip
          pkgs.zip
        ];

        JAVA_HOME = pkgs.zulu17.home;
        ANDROID_HOME = "${androidSdk}/libexec/android-sdk";
        ANDROID_SDK_ROOT = "${androidSdk}/libexec/android-sdk";
        GRADLE_OPTS = "-Dorg.gradle.project.android.aapt2FromMavenOverride=${androidSdk}/libexec/android-sdk/build-tools/35.0.0/aapt2";
      };
    };
}
