Pod::Spec.new do |s|
  s.name         = "RNInstantMessage"
  s.version      = "0.0.1"
  s.summary      = "A short description of RNInstantMessage."
  s.description  = "This is instant message library."
  s.homepage     = "https://github.com/sunny635533/react-native-instant-message"
  s.license      = "MIT"
  s.author             = { "sunny" => "1604587251@qq.com" }
  s.platform     = :ios, "8.0"
  s.source       = { :git => "https://github.com/sunny635533/react-native-instant-message", :tag => "{s.version}" }
   s.source_files  = "ios/*.{h,m}"
s.dependency 'React'
end
