def copyright = """/*
   Copyright 2014 Citrus Payment Solutions Pvt. Ltd.

   Licensed under the Apache License, Version 2.0 (the \"License\");
   you may not use this file except in compliance with the License.
   You may obtain a copy of the License at

     http://www.apache.org/licenses/LICENSE-2.0

   Unless required by applicable law or agreed to in writing, software
   distributed under the License is distributed on an \"AS IS\" BASIS,
   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
   See the License for the specific language governing permissions and
   limitations under the License.
*/

"""

def nocr(content) {
    (content =~ /(?s)\/\*[^\/]*Copyright[^\/]*\*\//).replaceAll('')
}

new File('src').eachFileRecurse {
    if (it.name.endsWith('.java')) {
        println it.path
        def content = copyright + nocr(it.text)
        it.write(content)
    }
}
