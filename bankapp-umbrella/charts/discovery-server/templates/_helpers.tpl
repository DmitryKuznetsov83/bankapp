cl{{- define "discovery-server.name" -}}
{{ .Chart.Name }}
{{- end }}

{{- define "discovery-server.fullname" -}}
{{ .Release.Name }}-{{ .Chart.Name }}
{{- end }}

{{- define "discovery-server.selectorLabels" -}}
app: {{ include "discovery-server.name" . }}
{{- end }}

{{- define "discovery-server.labels" -}}
app: {{ include "discovery-server.name" . }}
chart: {{ .Chart.Name }}-{{ .Chart.Version }}
release: {{ .Release.Name }}
heritage: {{ .Release.Service }}
{{- end }}