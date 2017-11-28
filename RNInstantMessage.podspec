Pod::Spec.new do |s|
  s.name         = "RNInstantMessage"
  s.version      = "0.0.1"
  s.summary      = "A short description of RNInstantMessage."
  s.description  = "This is instant message library."
  s.homepage     = "http://EXAMPLE/RNInstantMessage"
  s.license      = "MIT"
  s.author             = { "sunny" => "1604587251@qq.com" }
  s.platform     = :ios, "8.0"
  s.source       = { :git => "http://EXAMPLE/RNInstantMessage.git", :tag => "#{s.version}" }
   s.source_files  = "ios/*.{h,m}"
end
