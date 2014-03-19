Name:           osmpbf
Version:        1.3.3
Release:        1%{?dist}
Summary:        C version of the PBF library code
License:        LGPL-3+
URL:            https://github.com/scrosby/OSM-binary     
Source0:        OSM-binary-%{version}.tar.bz2
BuildRequires:  cmake, protobuf-devel, protobuf-lite-devel
BuildRequires:  gcc, gcc-c++

%description
Osmpbf is a Java/C library to read and write OpenStreetMap PBF files.
PBF (Protocol buffer Binary Format) is a binary file format for OpenStreetMap
data that uses Google Protocol Buffers as low-level storage.

For more information see http://wiki.openstreetmap.org/wiki/PBF_Format
 
%prep
%setup -q -n OSM-binary-%{version}
 
%build
cmake -DCMAKE_SKIP_RPATH=ON \
      -DCMAKE_INSTALL_PREFIX=%{_prefix}\
      -DLIBRARY_OUTPUT_PATH:PATH=%{_libdir} .
 
%{__make} %{?jobs:-j%jobs}
 
%install
%{__make} DESTDIR=%{buildroot} install
mkdir %{buildroot}/usr/lib64
mv %{buildroot}/usr/lib/libosmpbf.a %{buildroot}/usr/lib64
rm -rf %{buildroot}/usr/lib/
 
%files
%defattr(-, root, root, -)
%{_includedir}/%{name}
%{_libdir}/libosmpbf.a
%{_bindir}/osmpbf-outline

%changelog
* Wed Mar 19 2014 <kay.diam@gmail.com> - 1.3.3-1
- Bump version

* Mon Sep 23 2013 <kay.diam@gmail.com> - 1.3.1-1
- Initial release
